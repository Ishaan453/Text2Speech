import boto3
import string
import random
import json

def lambda_handler(event, context):
    text = event['text']
    speaker = event['speaker']

    polly = boto3.client('polly')
    s3 = boto3.client('s3')
    
    random_string = ''.join(random.choices(string.ascii_lowercase + string.digits, k=5))
    MP3_Name = f"{random_string}.mp3"
    print(MP3_Name)
    try:
        # Synthesize speech using Polly
        response = polly.synthesize_speech(Text=text, OutputFormat='mp3', VoiceId=speaker)

        # Store audio in a temporary S3 bucket
        s3.put_object(
            Bucket='temp-audio-text-to-speech',
            Key=MP3_Name, 
            Body=response['AudioStream'].read()
        )


        # Generate a presigned URL for the audio file
        presigned_url = s3.generate_presigned_url(
            ClientMethod='get_object',
            Params={'Bucket': 'temp-audio-text-to-speech', 'Key': MP3_Name},
            ExpiresIn=3600 
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
