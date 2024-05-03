from flask import Flask
from .settings import Config
# Importar outras dependências necessárias

def create_app(config_object=Config):
    # Cria uma instância do aplicativo Flask
    app = Flask(__name__)

    # Carregar as configurações do objeto de configuração
    app.config.from_object(config_object)

    # Registrar blueprints
    from .routes.recommendation_routes import recommendation_blueprint
    app.register_blueprint(recommendation_blueprint)

    # Configurar manipulação de erros, logging, autenticação se necessário

    # Retornar a instância do aplicativo configurada
    return app
