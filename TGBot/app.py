import logging
import time
from threading import Thread
from urllib import request

from flask import *
import telebot

from config import webhook_url
from modules.function import bot, MessageSender, CodeGen

flask_app = Flask(__name__)

@flask_app.route('/sethook')
def set_hook():
    bot.set_webhook(webhook_url)
    logging.info('webhook set to', webhook_url)
    return 'hook successfully set'


@flask_app.route('/hook', methods=['POST'])
def telegram():
    bot.process_new_updates([telebot.types.Update.de_json(request.stream.read().decode("utf-8"))])
    return "!", 200


@flask_app.route('/')
def main():
    return 'Hello world!'


if __name__ == '__main__':
    flask_app.run()

    while True:
        Thread(target=lambda: CodeGen().checkdate()).start()

        Thread(target=lambda: MessageSender().start()).start()

        time.sleep(5)
