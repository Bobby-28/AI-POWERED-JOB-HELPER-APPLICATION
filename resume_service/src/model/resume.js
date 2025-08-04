import mongoose from "mongoose";

const resumeSchema= new mongoose.Schema({
    usename: {
        type: String
    },
    skills: {
        type: [String]
    },
    expirence: {
        type: String
    },
    education: {
        type: String
    },
    userId: {
        type: String
    }
},{
    timestamps: true
});

export const ResumeModel= mongoose.model("Resume", resumeSchema);