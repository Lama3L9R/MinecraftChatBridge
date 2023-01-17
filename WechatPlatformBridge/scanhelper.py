import requests
import re
from PIL import Image
import base64

MAGIC = "Go8FCIkFEokFCggwMDAwMDAwMRAGGvAESySibk50w5Wb3uTl2c2h64jVVrV7gNs06GFlWplHQbY/5FfiO++1yH4ykCyNPWKXmco+wfQzK5R98D3so7rJ5LmGFvBLjGceleySrc3SOf2Pc1gVehzJgODeS0lDL3/I/0S2SSE98YgKleq6Uqx6ndTy9yaL9qFxJL7eiA/R3SEfTaW1SBoSITIu+EEkXff+Pv8NHOk7N57rcGk1w0ZzRrQDkXTOXFN2iHYIzAAZPIOY45Lsh+A4slpgnDiaOvRtlQYCt97nmPLuTipOJ8Qc5pM7ZsOsAPPrCQL7nK0I7aPrFDF0q4ziUUKettzW8MrAaiVfmbD1/VkmLNVqqZVvBCtRblXb5FHmtS8FxnqCzYP4WFvz3T0TcrOqwLX1M/DQvcHaGGw0B0y4bZMs7lVScGBFxMj3vbFi2SRKbKhaitxHfYHAOAa0X7/MSS0RNAjdwoyGHeOepXOKY+h3iHeqCvgOH6LOifdHf/1aaZNwSkGotYnYScW8Yx63LnSwba7+hESrtPa/huRmB9KWvMCKbDThL/nne14hnL277EDCSocPu3rOSYjuB9gKSOdVmWsj9Dxb/iZIe+S6AiG29Esm+/eUacSba0k8wn5HhHg9d4tIcixrxveflc8vi2/wNQGVFNsGO6tB5WF0xf/plngOvQ1/ivGV/C1Qpdhzznh0ExAVJ6dwzNg7qIEBaw+BzTJTUuRcPk92Sn6QDn2Pu3mpONaEumacjW4w6ipPnPw+g2TfywJjeEcpSZaP4Q3YV5HG8D6UjWA4GSkBKculWpdCMadx0usMomsSS/74QgpYqcPkmamB4nVv1JxczYITIqItIKjD35IGKAUwAA=="


def getmidstring(html, start_str, end):
    start = html.find(start_str)
    if start >= 0:
        start += len(start_str)
        end = html.find(end, start)
        if end >= 0:
            return html[start:end].strip()


print("info: scanhelper v1.0")
print("info: start login on wx.qq.com with UOS patch")

uuid_regex = re.compile(r"((?<=(uuid =)).+)?((?<=(uuid=)).+)?", flags=re.IGNORECASE)

rep = requests.get(
    "https://login.wx.qq.com/jslogin?appid=wx782c26e4c19acffb&fun=new&lang=zh_CN"
)
print("info: jslogin=" + rep.text)
uuid = rep.text.replace(
    'window.QRLogin.code = 200; window.QRLogin.uuid = "', ""
).replace('";', "")

print("info: get uuid=" + uuid)

rep = requests.get("https://login.wx.qq.com/qrcode/" + uuid + "?t=webwx")

with open("tmp.jpg", "wb") as f:
    f.write(rep.content)
    f.close

with Image.open("tmp.jpg") as img:
    img.show()

input("\nALERT: press enter after scan with wx mobile client and click confirm login.")

rep = requests.get(
    "https://login.wx.qq.com/cgi-bin/mmwebwx-bin/login?tip=1&uuid=" + uuid
)
print("info: qr scan resule=" + rep.text)
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

print("info: login dataraw=" + rep.text)
skey = getmidstring(rep.text, "<skey>", "</skey>")
wxsid = getmidstring(rep.text, "<wxsid>", "</wxsid>")
wxuin = getmidstring(rep.text, "<wxuin>", "</wxuin>")
pass_ticket = getmidstring(rep.text, "<pass_ticket>", "</pass_ticket>")
cookie = rep.headers.get("set-cookie")

print("\n====== login complete ======")
print(f"skey: {skey}")
print(f"wxsid: {wxsid}")
print(f"wxuin: {wxuin}")
print(f"pass_ticket: {pass_ticket}")
print(f"cookie: {base64.b64encode(cookie.encode('utf-8')).decode('ascii')}")

print("\nApply these values to the bridge crendentials configuration.")
print("**** DO NOT SHARE YOUR CREDENTIALS WITH ANYONE ****")
