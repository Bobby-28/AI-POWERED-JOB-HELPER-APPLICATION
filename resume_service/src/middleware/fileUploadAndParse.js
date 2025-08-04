import multer from "multer";
import path from "path";
import fs from 'path';
import { Configuration, OpenAIApi } from "openai";

const uploadDir = path.join(__dirname, '../../uploads');
if(!fs.existsSync(uploadDir)) fs.mkdirSync(uploadDir);

const Storage = multer.diskStorage(
    {
        destination: (req, file, cb) => cb(null, uploadDir),
        filename: (req, file, cb) => {
            const unique = Date.now() + '-' + Math.round(Math.random() * 1e9);
            cb(null, unique + '-' + file.originalname);
        }
    }
);

const fileFilter = (req, file, cb) => {
    if(file.mimetype === 'application/pdf') cb(null, true);
    else cb(new Error('Only PDF files allowed'), false);
};

const upload = multer({ storage, fileFilter});

const configuration = new Configuration({
    apiKey: process.env.OPENAI_API_KEY
});

const openai = new OpenAIApi(configuration);

const uploadAndParseResume = [
    upload.single('resume'),

    async (req, res, next) => {
        try{
            if(!req.file){
                return res.status(400).json({error: 'No file uploaded'});
            }
            const filePath = path.join(uploadDir, req.file.filename);
            const pdfData = fs.readFileSync(filePath).toString('base64');

            const response = await openai.chat.completions.create({
                model: 'gpt-4o',
                messages: [
                    {
                        role: 'system',
                        content: `You are a resume parser. Extract the following fields as JSON: 
                        - name (string)
                        - email (string)
                        - phone (string)
                        - skills (array of strings)
                        - education (string or array)
                        - experience (string; if none, return "fresher"). 
                        Only return valid JSON and no explanation.`
                    },
                    {
                        role: 'user',
                        content: [
                            {
                                type: 'text',
                                text: 'Please parse this resume the data in JSON foramt.'
                            },
                            {
                                type: 'file',
                                file: {
                                    name: req.file.filename,
                                    mime_type: 'application/pdf',
                                    data: pdfData
                                }
                            }
                        ]
                    }
                ],
                temperature: 0.2,
                max_tokens: 1024
            });

            const parsedData= JSON.parse(response.choices[0].message.content);

            req.parsedResume = parsedData;
            next();
        } catch(err){
            console.error(err);
            return res.status(500).json({error: 'Failed to parse resume'});
        }
    }
];

export default uploadAndParseResume;
