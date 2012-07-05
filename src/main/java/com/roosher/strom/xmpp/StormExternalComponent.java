package com.roosher.strom.xmpp;

import org.jivesoftware.whack.ExternalComponentManager;

public class StormExternalComponent {
    
    public static ExternalComponentManager getExternalComponent() {
        ExternalComponentManager externalComponentManager = new ExternalComponentManager("localhost", 5275);
        return externalComponentManager;
    }
    
}
