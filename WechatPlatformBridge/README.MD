# WechatPlatformBridge

[![996.icu](https://img.shields.io/badge/link-996.icu-red.svg)](https://996.icu)
[![LICENSE](https://img.shields.io/badge/license-Anti%20996-blue.svg)](https://github.com/996icu/996.ICU/blob/master/LICENSE)

_官方支持的微信平台链接桥_

## 我是用户

### 首先，你需要使用同目录下的 `scanhelper.py` 来登录

1. 安装最新的 `python3`
2. 执行 `pip install requests`
3. 执行 `pip install pillow`
4. 使用 `python scanhelper.py` 登录

**_使用时需要注意_**

1. 绝对不要将这个界面截图给任何你不信任的人
2. 如果一定需要截图，可以先登陆一次正常的 PC 版/网页版，再回到这个窗口进行截图（切记不要再登陆一次）
3. 注意扫完码之后点完确认登陆（如果有，包括 5 秒异地登陆警告），都点完了再回来按下回车继续
4. 登陆成功后会提示 `login complete`，请尽快将下方提示的数据填写在配置文件里并启动服务器
5. Cookie 一项比较长，不要漏了

示例：一个正常的输出

```
======== login complete ========
skey: @crypt_92b6*****************12b7fa6b3e675600481b
wxsid: K4j8********ECvD
wxuin: 27******23
pass_ticket: MDE%2FoLGqsAT%2FkEKS5vdGoeGCe********OE2CzZAW6FZV9f2zvuSFsNO7YEQXN0Z
cookie: d3h1aW49Mjc0OTY4MjYyMzsgRG9*****************bTsgUGF0aD0vOyBFeHBpcmVzPVdl... (省略1000+字符)

Apply these values to the bridge crendentials configuration.
**** DO NOT SHARE YOUR CREDENTIALS WITH ANYONE ****
```

### 然后就是配置文件

配置文件已经在 ChatBridge 2.0.0 以后更新为 HOCON 格式
```hocon
wechat {
  lang {
    wechatFormat: "[%s] %s"
    playerJoinFormat: "[+] %s"
    playerLeaveFormat: "[-] %s"
    serverOnlineFormat: "[+] Server"
    serverOfflineFormat: "[-] Server"
  }

  credentials {
    cookie: """
      <Your cookie here>
    """
    wxuin: ""
    wxsid: ""
    skey: ""
    passTicket: ""
  }

  listenOn: ""
}
```

## 我是开发者，我想贡献代码

欢迎开 PR :)

## TODO

1. [ ] 添加从网页版导入 Session