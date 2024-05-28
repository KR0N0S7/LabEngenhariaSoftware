from transformers import AutoModelForCausalLM, AutoTokenizer, TrainingArguments, Trainer
from datasets import load_dataset, DatasetDict
import pandas as pd

# Load your dataset from a CSV or text file (replace with your actual file)
df = pd.read_csv("samples.csv")
dataset = DatasetDict({"train": load_dataset('csv', data_files={'train': "samples.csv"}, split='train[:90%]')})

# Model and Tokenizer
model_name = 'EleutherAI/gpt-neo-125M' 
tokenizer = AutoTokenizer.from_pretrained(model_name)
model = AutoModelForCausalLM.from_pretrained(model_name)

# Training Arguments
training_args = TrainingArguments(
    output_dir="./fine_tuned_gpt-neo-125M",  
    overwrite_output_dir=True,
    num_train_epochs=3,              # Adjust based on dataset size and performance
    per_device_train_batch_size=4,   # Adjust based on your GPU/CPU memory
    save_steps=10_000,              # Save a checkpoint every 10,000 steps
    save_total_limit=2,             # Keep only the last 2 checkpoints
)

# Define Trainer 
trainer = Trainer(
    model=model,
    args=training_args,
    train_dataset=dataset["train"],
    tokenizer=tokenizer,
)

# Fine-tune the model
trainer.train()

# Save the fine-tuned model
trainer.save_model()

# Load the fine-tuned model for inference 
fine_tuned_model = AutoModelForCausalLM.from_pretrained("./fine_tuned_gpt-neo-125M") 
fine_tuned_tokenizer = AutoTokenizer.from_pretrained("./fine_tuned_gpt-neo-125M") 

def get_chatbot_response(user_message):
    inputs = fine_tuned_tokenizer(user_message, return_tensors="pt")
    outputs = fine_tuned_model.generate(**inputs, max_length=100, do_sample=True, temperature=0.7)
    response = fine_tuned_tokenizer.decode(outputs[0], skip_special_tokens=True)
    return response
