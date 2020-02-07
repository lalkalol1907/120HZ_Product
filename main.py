import time

from TGBot.modules.function import *


@bot.message_handler(commands=['start'])  # Тут все ок и работает
def start(message):
    """
    Вызывается при отправке команды /start
    Отправляет пользователю клавиатуру(admin, pupil, noname)

    :param message:
    :return:
    """
    pupilkbd = PupilTGkeyboards()
    adminkbd = AdminTGkeyboards()
    nonamekbd = NonameTGKeyboards()

    Man = MansDataBaseTG()

    Man.ID = message.from_user.id
    Man.man = Man.identy(Man.ID)

    if Man.man == "pupil":
        bot.send_message(Man.ID, text="Вот функции, доступные тебе:", reply_markup=pupilkbd.p_k())
    elif Man.man == "noname":
        bot.send_message(Man.ID, text="Вот функции, доступные тебе:", reply_markup=nonamekbd.n_k())
    elif Man.man == "admin":
        bot.send_message(Man.ID, text="Вот функции, доступные тебе:", reply_markup=adminkbd.admin_k())


@bot.message_handler(content_types=['text'])
def text(message):
    """
    Когда приходит текстовое сообщение запускается данная функция
    :param message:
    :return:
    """

    Man = MansDataBaseTG()

    Man.ID = message.from_user.id
    Man.man = Man.identy(Man.ID)
    if Man.man == "noname":
        nn_f(message, Man)
    else:
        if Man.man != "admin":
            Man.acts = Man.GetUserActs(Man.ID)
        Man.VkId = Man.getVkId(Man.ID)


        if Man.man == "admin":
            admin_f(message, Man)
        elif Man.man == "pupil":
            pupil_f(message, Man)


def nn_f(message, Man):
    """
    Если пользователь не в БД, то вызывается эта функция
    Пользователю(noname) доступна только функция регистрации

    :param message:
    :param Man:
    :return:
    """
    nonamekbd = NonameTGKeyboards()
    Code = CodeCheck()

    if message.text == NonameTexts.gettext("Регистрация"):  # Регистрация ноунэйма(работает)
        bot.send_message(Man.ID, text=NonameTexts.gettext("Ввод кода"))
        bot.register_next_step_handler(message, Code.checkcode)
    else:
        bot.send_message(Man.ID, text=Unknown_command, reply_markup=nonamekbd.n_k())


def pupil_f(message, Man):
    """
    Если пользователь имеет метку 'pupil', то вызывается данная функция
    Возможности:
        Посмотреть свои активности
        Подписаться на активность
        Отписаться от активности
        Посмотрет/Изменить ВК

    :param message:
    :param Man:
    :return:
    """
    PupilTexts = getText("pupil")

    pupilkbd = PupilTGkeyboards()

    activities_kbd = pupilkbd.sub()
    desub_kbd = pupilkbd.desub_k(Man.ID)

    if Man.man == "pupil":
        if message.text == PupilTexts.gettext("Показать активности"):  # Вроде работает
            try:
                bot.send_message(Man.ID, '\n'.join(Man.GetUserActs(Man.ID)), reply_markup=pupilkbd.p_k())
            except:
                bot.send_message(Man.ID, PupilTexts.gettext("Активностей нет"))

        elif message.text == PupilTexts.gettext("Подписка на активность"):  # Работает
            bot.send_message(Man.ID, text=f'{PupilTexts.gettext("Ответ с выводом(подписка)")}',
                             reply_markup=activities_kbd)
            bot.register_next_step_handler(message, Man.subscribe)

        elif message.text == PupilTexts.gettext("Отписка от активности"):
            if desub_kbd:
                bot.send_message(Man.ID, text=f'{PupilTexts.gettext("Ответ с выводом(отписка)")}',
                                 reply_markup=desub_kbd)
                bot.register_next_step_handler(message, Man.desub)
            else:
                bot.send_message(Man.ID, PupilTexts.gettext("Активностей нет"))

        elif message.text == PupilTexts.gettext("Изменение данных"):
            if Man.VkId == 0:
                bot.send_message(Man.ID, text="Твой ВК: Не указан\n Введи ID, который выдал бот ВК:", reply_markup=backkbd())
            else:
                bot.send_message(Man.ID, text=f'Твой ВК: {Man.VkId}\n Введи ID, который выдал бот ВК:', reply_markup=backkbd())

            bot.register_next_step_handler(message, Man.changeVK)
        else:
            bot.send_message(Man.ID, text=Unknown_command, reply_markup=pupilkbd.p_k())


def admin_f(message, Man):
    """
    Если пользователь имеет метку 'admin', то вызывается данная функция
    Возможности:
        Посмотреть все активности
        Добавить активность
        Удалить активность
        Отправить сообщение активности
        Получить код регистрации

    :param message:
    :param Man:
    :return:
    """

    AdminTexts = getText("admin")

    Acts = ActsDataBase()

    adminkbd = AdminTGkeyboards()

    sendmsg = SendMessage()
    del_act_kbd = adminkbd.del_act_kb()
    sendkbd = adminkbd.send_message_kbd()

    if Man.man == "admin":
        if message.text == AdminTexts.gettext("Добавление активности"):  # Работает
            bot.send_message(Man.ID, text="Введите название:", reply_markup=backkbd())
            bot.register_next_step_handler(message, Acts.add_act)

        elif message.text == AdminTexts.gettext("Удаление активности"):  # Работает
            try:
                if not Acts.getallacts():
                    bot.send_message(Man.ID, "Активностей нет!", reply_markup=adminkbd.admin_k())
                else:
                    bot.send_message(Man.ID, "Выберите активность, которую хотите удалить:", reply_markup=del_act_kbd)
                    bot.register_next_step_handler(message, Acts.del_act)
            except TypeError:
                bot.send_message(Man.ID, "Активностей нет!",reply_markup=adminkbd.admin_k())

        elif message.text == AdminTexts.gettext("Просмотр активностей"):  # С багом, но работает
            try:
                if not Acts.getallacts():
                    bot.send_message(Man.ID, "Активностей нет!",reply_markup=adminkbd.admin_k())
                else:
                    bot.send_message(Man.ID, '\n'.join(Acts.getallacts()), reply_markup=adminkbd.admin_k())
            except TypeError:
                bot.send_message(Man.ID, "Активностей нет!", reply_markup=adminkbd.admin_k())

        elif message.text == AdminTexts.gettext("Отправка сообщения"):  # Работает
            bot.send_message(Man.ID, "Какой группе вы хотите отправить сообщение?:", reply_markup=sendkbd)
            bot.register_next_step_handler(message, sendmsg.send)

        elif message.text == AdminTexts.gettext("Получить код"):  # Работает
            bot.send_message(Man.ID, f'{CodeGen().getcode()}', reply_markup=adminkbd.admin_k())

        else:
            bot.send_message(Man.ID, text=Unknown_command, reply_markup=adminkbd.admin_k())

if __name__ == '__main__':
    Thread(target = lambda: bot.infinity_polling(True)).start()

    while True:
        Thread(target=lambda: CodeGen().checkdate()).start()
        Thread(target=lambda: MessageSender().start()).start()

        time.sleep(5)
