# Text-to-Speech Application

This is a Text-to-Speech (TTS) application that converts input text into an MP3 file using AWS Polly and stores it in an S3 bucket. The application provides a REST API that accepts text as input and returns a presigned URL for the generated audio file.

## Architecture Overview

![Architecture Diagram](https://github.com/Ishaan453/Text2Speech/blob/master/Images/architecture.png)

The architecture consists of the following components:

1. **Mobile Application:** The user initiates the text-to-speech conversion by clicking the "Convert" button in the mobile application.

2. **REST API:** The mobile application communicates with a REST API, sending a JSON payload with the input text. The API triggers an AWS Lambda function to process the request.

3. **AWS Lambda Function:** The Lambda function receives the text input, uses AWS Polly to convert it into an MP3 file, and then uploads the file to an S3 bucket. It generates a presigned URL for the stored MP3 file and returns it to the mobile application.

4. **Amazon Polly:** AWS Polly is used for text-to-speech conversion.

5. **Amazon S3:** The generated MP3 files are stored in an S3 bucket.

## Project Structure

The project structure is organized as follows:

- **Text2Speech/**
  - **API/**
    - `Speech`: Implementation of the REST API.
  - **Lambda/**
    - `lambda_function.py`: AWS Lambda function code for text-to-speech conversion.
  - **Architecture/**
    - `architecture.png`: Architecture diagram image.

## Application View
<img src="https://github.com/Ishaan453/Text2Speech/blob/master/Images/App1.jpg" alt="App Image 1" width="500"/>
