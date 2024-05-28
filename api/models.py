# API request and response models

from pydantic import BaseModel

class FraudDetectionRequest(BaseModel):
    # Define the fields expected in the API request based on your Java entity classes
    # Example:
    cliente: dict 
    compra: dict
    pagamento: dict
    # ... Add other relevant fields ...

class FraudDetectionResponse(BaseModel):
    is_fraud: int