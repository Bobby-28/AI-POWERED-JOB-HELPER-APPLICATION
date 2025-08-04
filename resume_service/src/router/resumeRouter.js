import express from 'express';
import { controller } from '../controller/resumeController';
import uploadAndParseResume from '../middleware/fileUploadAndParse';

const router= express.Router();

router.route("/").post(uploadAndParseResume, controller.resumeUpload);
router.route("/").get(controller.resumeData);
router.route("/").delete(controller.deleteResume);
router.route("/").put(uploadAndParseResume, controller.updateResume);

export default router;