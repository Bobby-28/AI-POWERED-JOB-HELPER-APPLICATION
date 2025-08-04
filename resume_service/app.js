import express from "express";
import cors from 'cors';
import router from "./src/router/resumeRouter";
import { Eureka } from "eureka-js-client";
import connectDb from "./src/repository/dbConnection";
const app= express();
app.use(express.json());
connectDb();
app.use(cors());
app.use("/api/v1/resume", router);
const client = new Eureka({
    instance: {
      app: 'cart-service',
      instanceId: 'cart-service-1',
      hostName: 'localhost',
      ipAddr: '127.0.0.1',
      port: {
        '$': process.env.PORT,
        '@enabled': true,
      },
      vipAddress: 'cart-service',
      dataCenterInfo: {
        '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo',
        name: 'MyOwn',
      },
    },
    eureka: {
      host: 'localhost',
      port: 8761,
      servicePath: '/eureka',
      maxRetries: 10,
      requestRetryDelay: 2000,
    },
  });
  
  client.start((error) => {
    if (error) {
      console.error('Eureka registration failed:', error);
    } else {
      console.log('Cart service registered with Eureka');
    }
  });
export default app;