from openpyxl import load_workbook
from random import randint
import telebot
from telebot import types
from TGbot.config import *
import pymysql
from TGbot.modules.Texts import *
from threading import *

Config = config()


class DataBase:

    def __init__(self):

        self.conargs = Config.conargs

    def get_blocks(self):
        con = pymysql.connect(**self.conargs)

        blocks = []
        with con.cursor() as cur:
            pass

        # Перебор массива блоков и включение в "blocks"
        con.close()
        return blocks

    def get_block_questions(self, block):
        questions = []

        con = pymysql.connect(**self.conargs)
        with con.cursor() as cur:
            pass

        con.close()
        return questions

    def get_answer(self, number=None, questions=None):
        ans = ""
        con = pymysql.connect(**self.conargs)

        if number:
            with con.cursor() as cur:
                pass
            pass  # Получаем ответ на вопрос с номером number
        elif questions:
            with con.cursor() as cur:
                pass
            pass  # Получаем ответ на вопрос questions

        con.close()
        return ans

    def get_all_questions(self):
        questions = []
        con = pymysql.connect(**self.conargs)
        with con.cursor() as cur:
            pass

        con.close()
        return questions


bot = telebot.TeleBot(f'{Config.TG_TOKEN}')
