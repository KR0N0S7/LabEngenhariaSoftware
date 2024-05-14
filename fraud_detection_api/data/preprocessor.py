from sklearn.preprocessing import StandardScaler

def preprocess_data(data):
    """
    Preprocess the input data for the model.

    Args:
        data (dict): Input data from the API request.

    Returns:
        dict: Preprocessed data for the model.
    """
    # 1. Extract relevant features from the nested dictionaries:
    features = []
    # Example: Extract features from 'cliente', 'compra', and 'pagamento' dictionaries
    cliente_features = [data["cliente"]["age"], data["cliente"]["income"]]
    compra_features = [data["compra"]["amount"], data["compra"]["number_of_items"]]
    pagamento_features = [data["pagamento"]["method"], data["pagamento"]["installments"]]
    features.extend(cliente_features)
    features.extend(compra_features)
    features.extend(pagamento_features)

    # 2. Encode categorical features (e.g., using one-hot encoding):
    # ... Implement encoding logic for categorical features like 'pagamento.method' ...

    # 3. Scale numerical features:
    scaler = StandardScaler()
    scaled_features = scaler.fit_transform([features])  # Fit and transform

    # 4. Return the preprocessed features as a dictionary or array:
    return scaled_features[0]  # Extract the single sample from the array