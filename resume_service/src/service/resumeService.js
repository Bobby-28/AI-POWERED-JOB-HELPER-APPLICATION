import { ResumeModel } from "../model/resume";

class Resume{
    async createResume(data, user_id){
        return await ResumeModel.create({ ...data, user_id});
    }
    async getResume(user_id){
        return await ResumeModel.find({user_id});
    }
    async deleteResume(user_id){
        return await ResumeModel.findOneAndDelete({user_id});
    }
    async updateResume(user_id, data){
        return await ResumeModel.findOneAndUpdate({user_id}, { ...data}, {new:true});
    }
}
export default new Resume