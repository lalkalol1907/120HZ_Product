from telebot import types
from TGbot.modules.function import *


class TGkeyboards:
    """
    Класс всех пользовательских клавиатур
    """

    def __init__(self):
        self.texts = Text()
        self.DB = DataBase()

    def blocks_kbd(self):
        kbd = types.ReplyKeyboardMarkup()
        blocks = self.DB.get_blocks()
        for i in range(0, len(blocks)):
            kbd.add(types.KeyboardButton(blocks[i]))
        return kbd

    @staticmethod
    def backkbd():
        """
        Клавиатура с кнопкой 'Назад'
        :return:
        """
        backkbd = types.ReplyKeyboardMarkup()
        backkbd.add("Назад")
        return backkbd

    @staticmethod
    def start_kbd():
        """
        клавиатура с кнопкой /start
        """
        st = types.ReplyKeyboardMarkup()
        st.row(types.KeyboardButton("/start"))
        return st

    def answer_kbd(self):
        kbd = types.ReplyKeyboardMarkup()
        kbd.add(f"{self.texts.ANSWER}")
        return kbd

    @staticmethod
    def yes_no_kbd():
        kbd = types.ReplyKeyboardMarkup()
        kbd.add("Да")
        kbd.add("Нет")
        return kbd

    def questions_kbd(self, block):
        """
        ВОПРОСЫ ДАННОГО БЛОКА, А ТО Я ЗАБУДУ ДЛЯ ЧЕГО
        Клавиатура в сообщении, не внизу!!! (27.09 - Возможно и внизу))
        """
        kbd = types.ReplyKeyboardMarkup()
        q = self.DB.get_block_questions(block)
        for i in range(0, len(q)):
            kbd.add(q[i])
        return kbd
