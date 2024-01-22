import boto3
import json

def lambda_handler(event, context):
    text = event['text']

    polly = boto3.client('polly')
    s3 = boto3.client('s3')

    try:
        # Synthesize speech using Polly
        response = polly.synthesize_speech(Text=text, OutputFormat='mp3', VoiceId='Joanna')
        print(text)
        print("Response:", response['AudioStream'])

        # Store audio in a temporary S3 bucket
        s3.put_object(
            Bucket='temp-audio-text-to-speech',  # Replace with your S3 bucket name
            Key='speech.mp3',  # Replace with your desired filename
            Body=response['AudioStream'].read()
        )


        # Generate a presigned URL for the audio file
        presigned_url = s3.generate_presigned_url(
            ClientMethod='get_object',
            Params={'Bucket': 'temp-audio-text-to-speech', 'Key': 'speech.mp3'},
            ExpiresIn=3600  # Set expiration time for the URL
        )

        return {
            'statusCode': 200,
            'body': presigned_url
        }

    except Exception as e:
        print(f"Error: {e}")
        return {
            'statusCode': 500,
            'body': 'Error processing text-to-speech'
        }
