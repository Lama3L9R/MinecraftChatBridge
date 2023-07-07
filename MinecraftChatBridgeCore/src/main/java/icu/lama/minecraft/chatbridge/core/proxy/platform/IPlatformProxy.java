package icu.lama.minecraft.chatbridge.core.proxy.platform;

import icu.lama.minecraft.chatbridge.core.proxy.IProxy;

import java.util.List;

public interface IPlatformProxy extends IProxy {
    List<PlatformFeature> getSupportedFeatures();

    List<INamed> getAllContacts();

    INamed getContact(String uniqueIdentifier);

    IProxyChatGroup getGroup(String uniqueIdentifier);

    IProxyChatMember getMember(String uniqueIdentifier);

    IProxyChatMember getMember(IProxyChatMember group, String uniqueIdentifier);

    IProxyChatMember getMember(String groupIdentifier, String uniqueIdentifier);

    String getPlatformName();

    void sendMessage(String uniqueIdentifier, String msg);

    Object unwrap();
}
