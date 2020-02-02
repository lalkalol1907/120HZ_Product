import configparser

tokenpath = "./Data/TG_TOKENS.ini"


def getToken(path):
    """
    Получение токена из файла конфига
    :param path:
    :return:
    """

    config = configparser.RawConfigParser()
    config.read(tokenpath)

    return config.get('Config', 'Config.test_token')


TG_TOKEN = getToken(tokenpath)

TG_API_URL = ""

webhook_url = 'http://lalkalol.beget.tech/hook'
