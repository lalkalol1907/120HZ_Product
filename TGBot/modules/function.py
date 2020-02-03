from openpyxl import load_workbook
from random import randint
import telebot
from telebot import types
from TGBot.config import *
import datetime
import time
from TGBot.Telegram.keyboard import PupilTGkeyboards, AdminTGkeyboards, NonameTGKeyboards, backkbd
import pymysql
from TGBot.modules.getText import *
from threading import *

# Аргументы подключения к бд
conargs = {
    'host': 'localhost',
    'user': 'lalkalol_crochz',
    'password': 'python',
    'db': 'lalkalol_crochz'
}

# Тексты
AdminTexts = getText("admin")
PupilTexts = getText("pupil")
NonameTexts = getText("noname")
OthersTexts = getText("other")
Unknown_command = OthersTexts.gettext("Unknown command")


def start_kbd():
    """
    :param start_kbd() - клавиатура с кнопкой /start
    """
    st = types.ReplyKeyboardMarkup()
    row = types.KeyboardButton("/start")
    st.row(row)
    return st


bot = telebot.TeleBot(f'{TG_TOKEN}')


adminkbd = AdminTGkeyboards.admin_k()
nonamekbd = NonameTGKeyboards.n_k()
pupilkbd = PupilTGkeyboards.p_k()


class MansDataBase(object):

    @staticmethod
    def nonone(a):
        """
        Удаление повторений в массиве
        :param a:
        :return:
        """
        n = []
        for i in a:
            if i not in n:
                n.append(i)
        return n

    def __init__(self):
        self.ID = ""
        self.man = ""
        self.acts = []
        self.VkId = ""


class MansDataBaseTG(MansDataBase):
    """
    База данных пользователей
    """

    def delActs(self, name):
        """
        Удаляет активности у людей, подписанных на удаляемую активность
        :param name:
        :return:
        """
        con = pymysql.connect(**conargs)

        sql = f"UPDATE MainTableV1 SET acts = '' WHERE acts = %s"

        with con:
            cur = con.cursor()
            cur.execute(sql, name)
        con.close()

    def todb(self, id):
        """
        Добавляет пользователя в базу данных
        :param id:
        :return:
        """
        con = pymysql.connect(**conargs)

        self.ID = id
        sql = f"INSERT INTO MainTableV1 VALUES(%s, 'pupil','','')"
        with con:
            cur = con.cursor()
            cur.execute(sql, self.ID)
        con.close()

        bot.send_message(self.ID, "Ты успешно зарегистрирован", reply_markup=pupilkbd)

    def identy(self, ID):
        """
        Определение статуса человека(admin/pupil/noname) по его ID
        :param ID:
        :return:
        """

        con = pymysql.connect(**conargs)

        IdArray = []
        ManArray = []
        status = "noname"
        sql = f"SELECT TgId, Man FROM MainTableV1 WHERE TgId = %s"
        with con:
            cur = con.cursor()
            cur.execute(sql, ID)
            rows = cur.fetchall()
            for row in rows:
                TgId, Man = row
                IdArray.append(TgId)
                ManArray.append(Man)
        for i in range(0, len(IdArray)):
            status = ManArray[i]
        con.close()
        return status

    def GetUserActs(self, ID):
        """
        Возвращает все активности, на которые подписн пользователь
        :param ID:
        :return:
        """
        con = pymysql.connect(**conargs)

        actsArray = []
        sql = f"SELECT acts FROM MainTableV1 WHERE TgId = %s"
        with con:
            cur = con.cursor()
            cur.execute(sql, int(ID))
            rows = cur.fetchall()
            for row in rows:
                acts = row[0]
                actsArray.append(acts)
        con.close()

        return self.nonone(actsArray)

    def getVkId(self, ID):
        """
        Возвращает ВК ID человека, если оно отсутствует возвращает 'Не указано'

        :param ID:
        :return:
        """

        con = pymysql.connect(**conargs)

        VkId_status = ""
        sql = f"SELECT VkId FROM MainTableV1 WHERE TgId = %s"
        with con:
            cur = con.cursor()
            cur.execute(sql, int(ID))
            rows = cur.fetchall()
            for row in rows:
                VkId_status = row[0]

        con.close()

        if VkId_status != 0:
            return VkId_status
        else:
            return "Не указано"

    def subscribe(self, message):  # Подписка на активность
        """
        Функции подписки человека на активность, указанную в сообщении

        :param message:
        :return:
        """

        if message.text == "Назад":
            bot.send_message(message.from_user.id, text=OthersTexts.gettext('back'), reply_markup=pupilkbd)
        else:

            id = message.from_user.id
            useracts = self.GetUserActs(id)
            c = True
            b = True
            if useracts == None:
                b = False
            activ = message.text
            a = False
            if b:
                if activ in useracts:
                    bot.send_message(id, f'Ошибка! Ты уже зарегистрирован на активность «{activ}»',
                                     reply_markup=pupilkbd)
                    c = False
                if not activ in ActsDataBase().getallacts():
                    c = False
                    bot.send_message(id, f'Ошибка! Активности «{activ}» не существует', reply_markup=pupilkbd)
            if c:
                con = pymysql.connect(**conargs)
                with con:
                    cur = con.cursor()
                    self.VkId = self.getVkId(self.ID)
                    if self.VkId == "Не указано":
                        self.VkId = ""
                    sql = f"INSERT INTO MainTableV1 VALUES(%s, 'pupil' ,%s , %s)"

                    sqlt = [int(self.ID), activ, self.VkId]

                    cur.execute(sql, sqlt)

                con.close()

                bot.send_message(id, f"Ты подписался на активность «{activ}»", reply_markup=pupilkbd)

    def desub(self, message):
        """
        Функция отписки человека от активности, указанной в сообщении

        :param message:
        :return:
        """
        if message.text == "Назад":
            bot.send_message(message.from_user.id, text=OthersTexts.gettext('back'), reply_markup=pupilkbd)
        else:
            self.ID = message.from_user.id
            activ = message.text
            self.acts = self.GetUserActs(self.ID)

            self.VkId = self.getVkId(self.ID)
            if self.VkId == "Не указано":
                self.VkId = ""

            if activ == "Отписаться от всех активностей":
                sql = f"DELETE FROM MainTableV1 WHERE TgId = %s"
                sqlt = self.ID
            elif activ in self.acts:
                sql = f"DELETE FROM MainTableV1 WHERE TgId = %s AND acts = %s"
                sqlt = [self.ID, activ]
            else:
                bot.send_message(self.ID, "Ошибка! Ты не подписан на активность «{activ}»", reply_markup=pupilkbd)
                return 0

            con = pymysql.connect(**conargs)

            with con:
                cur = con.cursor()
                cur.execute(sql, sqlt)
                if self.identy(self.ID) == "noname":
                    sql = f"INSERT INTO MainTableV1 VALUES(%s, 'pupil','', %s)"
                    sqlt = [self.ID, self.getVkId(self.ID)]
                    cur.execute(sql, sqlt)
                if activ != "Отписаться от всех активностей":
                    bot.send_message(self.ID, f"Ты отписался от активности «{activ}»", reply_markup=pupilkbd)
                else:
                    bot.send_message(self.ID, f"Ты отписался от всех активностей", reply_markup=pupilkbd)

            con.close()

    def changeVK(self, message):
        """
        ФФункция изменения ВК человека

        :param message:
        :return:
        """

        self.ID = message.from_user.id
        if message.text == "Назад":
            bot.send_message(self.ID, text=OthersTexts.gettext('back'), reply_markup=pupilkbd)
        else:
            try:
                con = pymysql.connect(**conargs)
                self.VkId = int(message.text)
                with con:
                    cur = con.cursor()
                    sql = f"UPDATE MainTableV1 SET VkId = %s WHERE TgId = %s"
                    sqlt = [self.VkId, self.ID]
                    cur.execute(sql, sqlt)
                con.close()
                bot.send_message(self.ID, "Успешная смена ВК", reply_markup=pupilkbd)
            except ValueError:
                bot.send_message(self.ID, "Некорректный ID", reply_markup=pupilkbd)


class ActsDataBase:
    """
    Класс для работы с базой данных активностей
    """

    @staticmethod
    def nonone(a):
        """
        Удаление повторений в массиве
        :param a:
        :return:
        """
        n = []
        for i in a:
            if i not in n:
                n.append(i)
        return n

    def getallacts(self):
        """
        Возвращает все активности
        :return:
        """

        con = pymysql.connect(**conargs)

        activities = []
        sql = "SELECT * FROM Acts"
        with con:
            cur = con.cursor()
            cur.execute(sql)
            rows = cur.fetchall()
            for row in rows:
                act = row[0]
                activities.append(act)

        con.close()

        return self.nonone(activities)

    def add_act(self, message):
        """
        Функция добаляет активность, указанную в сообщении, в БД
        :param message:
        :return:
        """

        con = pymysql.connect(**conargs)

        if message.text == "Назад":
            bot.send_message(message.from_user.id, text=OthersTexts.gettext('back'), reply_markup=adminkbd)
        else:
            name = message.text
            sql = f"INSERT INTO Acts VALUES(%s)"
            with con:
                cur = con.cursor()
                cur.execute(sql, name)

            con.close()
            bot.send_message(message.from_user.id, f"Активность «{name}» добавлена", reply_markup=adminkbd)

    def del_act(self, message):
        """
        Функция удаляет активность, указанную в сообщении, из БД

        :param message:
        :return:
        """
        if message.text == "Назад":
            bot.send_message(message.from_user.id, text=OthersTexts.gettext('back'), reply_markup=adminkbd)
        elif message.text in self.getallacts():
            name = message.text
            sql = f"DELETE FROM Acts WHERE name = %s"
            con = pymysql.connect(**conargs)

            with con:
                cur = con.cursor()
                cur.execute(sql, name)
            con.close()

            bot.send_message(message.from_user.id, f"Активность «{name}» удалена", reply_markup=adminkbd)

            MansDataBaseTG().delActs(name)  # Удаляем активности из БД людей

        else:
            bot.send_message(message.from_user.id, f"Активности «{message.text}» не существует", reply_markup=adminkbd)


class SendMessage:

    @staticmethod
    def nonone(a):
        """
        Удаление повторений в массиве
        :param a:
        :return:
        """
        n = []
        for i in a:
            if i not in n:
                n.append(i)
        return n

    def __init__(self):
        self.text = ""
        self.group = []

    def GetTgIds(self, act):
        """
        Функция получает все ТГ ИД людей, подписанных на данную активность
        :param act:
        :return:
        """
        con = pymysql.connect(**conargs)
        arr = []
        if act == "Всем":
            sql = "SELECT TgId FROM MainTableV1"
        else:
            sql = f"SELECT TgId FROM MainTableV1 WHERE acts = '{act}'"
        with con:
            cur = con.cursor()
            cur.execute(sql)
            rows = cur.fetchall()
            for row in rows:
                arr.append(row[0])
        con.close()
        return self.nonone(arr)

    def send(self, message):
        """
        Функция достает название активности из текста
        :param message:
        :return:
        """
        if message.text == "Назад":
            bot.send_message(message.from_user.id, text=OthersTexts.gettext('back'), reply_markup=adminkbd)
        elif message.text in ActsDataBase().getallacts():
            act = message.text
            bot.send_message(message.from_user.id, "Введите сообщение:", reply_markup=backkbd())

            bot.register_next_step_handler(message, self.send2, act)
        else:
            bot.send_message(message.from_user.id, text=f"Активности «{message.text}» не существует", reply_markup=adminkbd)

    def GetLastId(self):
        """
        Получает id последнего запроса отправки сообщения
        :return:
        """
        con = pymysql.connect(**conargs)
        sql = "SELECT * FROM SendList"
        lastId = 0
        with con:
            cur = con.cursor()
            cur.execute(sql)
            rows = cur.fetchall()
            for row in rows:
                lastId = row[0]

        con.close()
        return lastId

    def send2(self, message, act):
        """
        Добаляет в БД отправки сообщений запрос

        :param message: текст сообщения
        :param act: активность
        :return:
        """
        if message.text == "Назад":
            bot.send_message(message.from_user.id, text=OthersTexts.gettext('back'), reply_markup=adminkbd)
        else:
            con = pymysql.connect(**conargs)
            self.text = message.text
            sql = f"INSERT INTO SendList VALUES('{self.GetLastId() + 1}', '{act}', '{self.text}', '0', '0')"
            with con:
                cur = con.cursor()
                cur.execute(sql)
            con.close()

            bot.send_message(message.from_user.id, f"Сообщение отправлено активности «{act}»", reply_markup=adminkbd)


class CodeCheck:
    """
    Класс проверяет код при регистрации человека

    Структура таблицы:
        0: id (номер кода, необходим для проверки последнего)
        1: code (сам код)
        2: day (день в который был создан)
    """

    def __init__(self):
        self.MainDB = MansDataBaseTG()


    def code(self):
        """
        Достает правильный код из таблицы
        :return:
        """
        return CodeGen().getcode()

    def checkcode(self, message):
        """
        Проверяет соответствие кода сообщения(message.text) и верного кода
        Если верно, то вызывает функцию добавления в БД

        :param message:
        :return:
        """
        if message.text == "Назад":
            bot.send_message(message.from_user.id, text=OthersTexts.gettext('back'), reply_markup=nonamekbd)
        else:
            inputCode = message.text
            Man = MansDataBaseTG()

            Man.ID = message.from_user.id
            if inputCode == self.code():
                self.MainDB.todb(Man.ID)
            else:
                bot.send_message(Man.ID, text="Ты ввел неверный код!", reply_markup=nonamekbd)


class CodeGen:
    """
    Класс отвечает за обновление кода каждый день

    Структура таблицы:
        0: id (номер кода, необходим для проверки последнего)
        1: code (сам код)
        2: day (день в который был создан)
    """

    def __init__(self):
        self.now = datetime.datetime.now()

    @staticmethod
    def GetLastCodeId():
        """
        Получает id последнего кода
        :return:
        """
        con = pymysql.connect(**conargs)
        sql = "SELECT * FROM CodeList"
        lastId = 0
        with con:
            cur = con.cursor()
            cur.execute(sql)
            rows = cur.fetchall()
            for row in rows:
                lastId = row[0]

        con.close()
        return lastId

    def codegen(self):
        """
        Генерирует рандомной 5-ти значное число, которое является кодом и заносит его в таблицу
        :return:
        """
        con = pymysql.connect(**conargs)
        code = randint(10000, 99999)
        with con.cursor() as cur:
            cur.execute(f"INSERT INTO CodeTable VALUES('{self.GetLastCodeId() + 1}', '{code}', '{self.now.day}')")
        con.close()

    def getcode(self):
        """
        получает код из БД
        :return:
        """
        con = pymysql.connect(**conargs)
        with con.cursor() as cur:
            cur.execute(f"SELECT * FROM CodeTable WHERE id = '{self.GetLastCodeId()}'")
            rows = cur.fetchall()
            for row in rows:
                code = row[1]
        con.close()
        return code

    def checkdate(self):
        """
        Сравнивает дату в таблице и текущую
        Если отличается - запускает codegen()
        :return:
        """
        con = pymysql.connect(**conargs)
        with con.cursor() as cur:
            cur.execute(f"SELECT * FROM CodeTable WHERE id = '{self.GetLastCodeId()}'")
            rows = cur.fetchall()
            for row in rows:
                last_day = row[2]
        con.close()

        day = self.now.day
        if str(last_day) != str(day):
            self.codegen()
        else:
            pass


class MessageSender:
    """
    0: id
    1: act - Имя активности
    2: text - Текст сообщения
    3: DoneTg - 1/0 Выполнено или нет (ТГ)
    4: DoneVk - 1/0 Выполнено или нет (ВК)

    """

    def __init__(self):
        self.ID = ""
        self.act = ""
        self.text = ""
        self.TgIds = []

    def start(self):
        con = pymysql.connect(**conargs)
        sql = "SELECT * FROM SendList"
        with con:
            cur = con.cursor()
            cur.execute(sql)
            rows = cur.fetchall()
            for row in rows:
                if int(row[3]) == int(0):
                    self.ID = row[0]
                    self.act = row[1]
                    self.text = row[2]
                    self.TgIds = SendMessage().GetTgIds(self.act)
                    sqld = f"UPDATE SendList SET DoneTg = '1' WHERE id = '{self.ID}'"
                    cur.execute(sqld)
                    for i in range(len(self.TgIds)):
                        try:
                            if self.TgIds[i]:
                                bot.send_message(self.TgIds[i], self.text + f'\n\nДля активности: «{self.act}»')
                        except:
                            pass
        con.close()
