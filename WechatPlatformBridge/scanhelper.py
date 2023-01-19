import re
import os
import sys
import base64
import logging
import requests
import subprocess

logging.basicConfig(
    level=logging.INFO,
    format="[%(asctime)s] [%(levelname)s]: %(message)s",
    datefmt="%Y-%m-%d %H:%M:%S",
)

MAGIC = "Go8FCIkFEokFCggwMDAwMDAwMRAGGvAESySibk50w5Wb3uTl2c2h64jVVrV7gNs06GFlWplHQbY/5FfiO++1yH4ykCyNPWKXmco+wfQzK5R98D3so7rJ5LmGFvBLjGceleySrc3SOf2Pc1gVehzJgODeS0lDL3/I/0S2SSE98YgKleq6Uqx6ndTy9yaL9qFxJL7eiA/R3SEfTaW1SBoSITIu+EEkXff+Pv8NHOk7N57rcGk1w0ZzRrQDkXTOXFN2iHYIzAAZPIOY45Lsh+A4slpgnDiaOvRtlQYCt97nmPLuTipOJ8Qc5pM7ZsOsAPPrCQL7nK0I7aPrFDF0q4ziUUKettzW8MrAaiVfmbD1/VkmLNVqqZVvBCtRblXb5FHmtS8FxnqCzYP4WFvz3T0TcrOqwLX1M/DQvcHaGGw0B0y4bZMs7lVScGBFxMj3vbFi2SRKbKhaitxHfYHAOAa0X7/MSS0RNAjdwoyGHeOepXOKY+h3iHeqCvgOH6LOifdHf/1aaZNwSkGotYnYScW8Yx63LnSwba7+hESrtPa/huRmB9KWvMCKbDThL/nne14hnL277EDCSocPu3rOSYjuB9gKSOdVmWsj9Dxb/iZIe+S6AiG29Esm+/eUacSba0k8wn5HhHg9d4tIcixrxveflc8vi2/wNQGVFNsGO6tB5WF0xf/plngOvQ1/ivGV/C1Qpdhzznh0ExAVJ6dwzNg7qIEBaw+BzTJTUuRcPk92Sn6QDn2Pu3mpONaEumacjW4w6ipPnPw+g2TfywJjeEcpSZaP4Q3YV5HG8D6UjWA4GSkBKculWpdCMadx0usMomsSS/74QgpYqcPkmamB4nVv1JxczYITIqItIKjD35IGKAUwAA=="


def substrBetween(str, start, end):
    start_index = str.find(start)
    if start_index >= 0:
        start_index += len(start)
        end_index = str.find(end, start_index)
        if end_index >= 0:
            return str[start_index:end_index].strip()


logging.info("scanhelper v1.0")
logging.info("start login on wx.qq.com with UOS patch")

uuid_regex = re.compile(r"((?<=(uuid =)).+)?((?<=(uuid=)).+)?", flags=re.IGNORECASE)

rep = requests.get(
    "https://login.wx.qq.com/jslogin?appid=wx782c26e4c19acffb&fun=new&lang=zh_CN"
)
logging.debug("jslogin=" + rep.text)

uuid = rep.text.replace(
    'window.QRLogin.code = 200; window.QRLogin.uuid = "', ""
).replace('";', "")
logging.debug("got uuid=" + uuid)

rep = requests.get("https://login.wx.qq.com/qrcode/" + uuid + "?t=webwx")

with open("login_qrcode.jpg", "wb") as f:
    f.write(rep.content)
    f.close()

if sys.platform == "win32":
    os.startfile("login_qrcode.jpg")
elif sys.platform == "darwin":
    subprocess.call(["open", "login_qrcode.jpg"])
else:
    subprocess.call(["xdg-open", "login_qrcode.jpg"])

input("\nALERT: press ENTER after login is confirmed on mobile.")

rep = requests.get(
    "https://login.wx.qq.com/cgi-bin/mmwebwx-bin/login?tip=1&uuid=" + uuid
)
logging.debug("qr scan result=" + rep.text)

newlogin = (
    rep.text.replace(" ", "")
    .replace("\n", "")
    .replace('window.code=200;window.redirect_uri="', "")
    .replace('";', "")
)

rep = requests.get(
    newlogin + "&version=v2&fun=new&mod=desktop&target=t",
    headers={
        "extspam": MAGIC,
        "client-version": "2.0.0",
    },
)

logging.debug("login dataraw=" + rep.text)

skey = substrBetween(rep.text, "<skey>", "</skey>")
wxsid = substrBetween(rep.text, "<wxsid>", "</wxsid>")
wxuin = substrBetween(rep.text, "<wxuin>", "</wxuin>")
pass_ticket = substrBetween(rep.text, "<pass_ticket>", "</pass_ticket>")
cookie = rep.headers.get("set-cookie")

print(
    f"""
======== login complete ========
skey: {skey}
wxsid: {wxsid}
wxuin: {wxuin}
pass_ticket: {pass_ticket}
cookie: {base64.b64encode(cookie.encode('utf-8')).decode('ascii')}
"""
)

print("Apply these values to the bridge credentials configuration.")
print("**** DO NOT SHARE YOUR CREDENTIALS WITH ANYONE ****")
