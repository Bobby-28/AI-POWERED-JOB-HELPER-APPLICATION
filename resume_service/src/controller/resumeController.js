import jwtService from "../service/jwtService";
import resumeService from "../service/resumeService";
import asyncHandler from 'express-async-handler';

const resumeUpload = asyncHandler(
    async (req, res) => {
        const userId= tokenValidation(req.headers);
        if(userId == null){
            res.status(401).json(
                {message: 'Authorization token missing or malframed'}
            )
        }
        const parsedData= req.parsedResume;
        const saved = await resumeService.createResume(parsedData, userId);
        res.status(201).json({
            message: 'Resume uploaded and data saved successfully',
            data: saved
        });
    }
)

const resumeData= asyncHandler(
    async(req, res) =>{
        const userId= tokenValidation(req.headers);
        if(userId == null){
            res.status(401).json(
                {message: 'Authorization token missing or malframed'}
            )
        }
        const data= await resumeService.getResume(userId);
        res.status(200).json(data);
    }
)

const deleteResume= asyncHandler(
    async(req, res) =>{
        const userId= tokenValidation(req.headers);
        if(userId == null){
            res.status(401).json(
                {message: 'Authorization token missing or malframed'}
            )
        }
        await resumeService.deleteResume(userId);
        res.status(200).json({
            message: 'The resume has been successfully deleted'
        })
    }
)

const updateResume= asyncHandler(
    async(req, res) =>{
        const userId= tokenValidation(req.headers);
        if(userId == null){
            res.status(401).json(
                {message: 'Authorization token missing or malframed'}
            )
        }
        const parseData= req.parsedResume;
        const update= await resumeService.updateResume(userId, parseData);
        res.status(200).json({
            message: 'Resume has been successfully updated',
            data: update
        })
    }
)

function tokenValidation(authHeader){
    if (!authHeader || !authHeader.startsWith('Bearer ')) {
        return null;
      }
    const token= authHeader.split(' ')[1];
    const userId= jwtService.extractUserId(token);
    return userId;
}

export const controller= {
    resumeUpload,
    resumeData,
    deleteResume,
    updateResume
}