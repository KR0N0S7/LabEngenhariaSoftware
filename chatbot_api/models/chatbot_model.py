from transformers import pipeline, AutoModelForCausalLM, AutoTokenizer

model_name = "gpt2" 
tokenizer = AutoTokenizer.from_pretrained(model_name)
model = AutoModelForCausalLM.from_pretrained(model_name) 

# Define a helper function for chatbot response generation
def get_chatbot_response(user_message):
    try:
        inputs = tokenizer(user_message, return_tensors='pt')
        outputs = model.generate(**inputs, max_length=50)
        response = tokenizer.decode(outputs[0], skip_special_tokens=True)
        return response
    except Exception as e:
        raise Exception(f"Error in generating response: {str(e)}")
