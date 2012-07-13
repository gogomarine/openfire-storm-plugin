<%@ page import="java.util.*,
                 com.roosher.storm.xmpp.plugin.Environments,org.jivesoftware.util.*"
%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>

<%
    boolean save = request.getParameter("save") != null;
    boolean reset = request.getParameter("reset") !=null;
    boolean success = request.getParameter("success") != null;
    
    //block list filter options/ cache options
    boolean blocklistEnabled = ParamUtils.getBooleanParameter(request, "blocklistEnabled");
    String [] openfireCacheEnabledChecked = ParamUtils.getParameters(request, "blocklistOpenfireCache");
    boolean openfireCacheEnabled = openfireCacheEnabledChecked.length > 0;
 
    //log options
    boolean allowLog = ParamUtils.getBooleanParameter(request, "allowLog");
    List<String> logMonitorPacketOptions = Arrays.asList(ParamUtils.getParameters(request, "logMonitorPacket"));
    boolean logIQEnabled = logMonitorPacketOptions.contains("iq");
    boolean logMessageEnabled = logMonitorPacketOptions.contains("message");
    boolean logPresenceEnabled = logMonitorPacketOptions.contains("presence");
    boolean logOtherEnabled = logMonitorPacketOptions.contains("other");
    
    boolean logPacketIncoming = logMonitorPacketOptions.contains("incoming");
    boolean logPacketOutter = logMonitorPacketOptions.contains("outter");
    boolean logPacketProcessing = logMonitorPacketOptions.contains("processing");
    boolean logPacketProcessed = logMonitorPacketOptions.contains("processed");
    
    
    //get handle to plugin
    Environments environments = Environments.getInstance();

    //input validation
    Map<String, String> errors = new HashMap<String, String>();
    if (save) {
    
        if (errors.size() == 0) {
            environments.setBlocklistFilterEnabled(blocklistEnabled);
            environments.setBlocklistOpenfireCacheEnabled(openfireCacheEnabled);
            //log
            environments.setLogMonitorEnabled(allowLog);
            environments.setLogMonitorMessageEnabled(logMessageEnabled);
            environments.setLogMonitorPresenceEnabled(logPresenceEnabled);
            environments.setLogMonitorIQEnabled(logIQEnabled);
            environments.setLogMonitorOtherEnabled(logOtherEnabled);
            
            environments.setLogMonitorIncomingEnabled(logPacketIncoming);
            environments.setLogMonitorOutterEnabled(logPacketOutter);
            environments.setLogMonitorProcessingEnabled(logPacketProcessing);
            environments.setLogMonitorProcessedEnabled(logPacketProcessed);
            
            response.sendRedirect("storm-props-admin.jsp?success=true");
            return;
        }
    } else if (reset) {
      environments.reset();
      response.sendRedirect("storm-props-admin.jsp?success=true");
    }
    
    blocklistEnabled = environments.isBlocklistFilterEnabled();
    openfireCacheEnabled = environments.isBlocklistOpenfireCacheEnabled();
    //log
    allowLog = environments.isLogMonitorEnabled();
    //log type choose.
    logMessageEnabled = environments.isLogMonitorMessageEnabled();
    logPresenceEnabled = environments.isLogMonitorPresenceEnabled();
    logIQEnabled = environments.isLogMonitorIQEnabled();
    logOtherEnabled = environments.isLogMonitorOtherEnabled();
    
    //log incoming or processed config.
    logPacketIncoming = environments.isLogMonitorIncomingEnabled();
    logPacketOutter = environments.isLogMonitorOutterEnabled();
    logPacketProcessing = environments.isLogMonitorProcessingEnabled();
    logPacketProcessed = environments.isLogMonitorProcessedEnabled();

%>

<html>
    <head>
        <title>Storm Blocklist</title>
        <meta name="pageID" content="storm-props-admin"/>
        <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    </head>
    <body>

<p>
Use the form below to edit Storm Blocklist settings.<br>
</p>

<%  if (success) { %>

    <div class="jive-success">
    <table cellpadding="0" cellspacing="0" border="0">
    <tbody>
        <tr>
            <td class="jive-icon"><img src="images/success-16x16.gif" width="16" height="16" border="0" alt=""></td>
            <td class="jive-icon-label">Settings updated successfully.</td>
        </tr>
    </tbody>
    </table>
    </div><br>

<%  } else if (errors.size() > 0) { %>

    <div class="jive-error">
    <table cellpadding="0" cellspacing="0" border="0">
    <tbody>
        <tr>
            <td class="jive-icon"><img src="images/error-16x16.gif" width="16" height="16" border="0" alt=""></td>
            <td class="jive-icon-label">Error saving the settings.</td>
        </tr>
    </tbody>
    </table>
    </div><br>

<%  } %>

<form action="storm-props-admin.jsp" method="post">

<fieldset>
    <legend>BlockList Filter</legend>
    <div>
    
    <p>
    To enable/disable blocklist filter, just choose options.
    </p>
    
    <table cellpadding="3" cellspacing="0" border="0" width="100%">
    <tbody>
        <tr>
            <td width="1%">
            <input type="radio" name="blocklistEnabled" value="false" id="not01"
             <%= ((blocklistEnabled) ? "" : "checked") %>>
        </td>
        <td width="99%">
            <label for="not01"><b>Disabled</b></label> - Blocklist will not work.
        </td>
    </tr>
    <tr>
        <td width="1%">
            <input type="radio" name="blocklistEnabled" value="true" id="not02"
             <%= ((blocklistEnabled) ? "checked" : "") %>>
        </td>
        <td width="99%">
            <label for="not02"><b>Enabled</b></label> - Blocklist will work.
        </td>
    </tr>
    <tr>
        <td>&nbsp;</td>
        <td><input type="checkbox" name="blocklistOpenfireCache" value="enabled" <%= openfireCacheEnabled ? "checked" : "" %>/>Use Openfire Cache for BlockList</td>
    </tr>
    </tbody>
    </table>
    </div>
</fieldset>

<br><br>

<fieldset>
    <legend>Log Monitor</legend>
    <div>
    
    <p>
    Configure this feature to disallow or allow packet content logged. Check out log with 'info.log'.
    Once main switch be disabled. All packet will not be logged.
    </p>
    
    <table cellpadding="3" cellspacing="0" border="0" width="100%">
    <tbody>
        <tr>
            <td width="1%">
            <input type="radio" name="allowLog" value="false" id="not03"
             <%= ((allowLog) ? "" : "checked") %>>
            </td>
            <td width="99%">
                <label for="not01"><b>Reject</b></label> - Packets will not be logged.
            </td>
        </tr>
        <tr>
            <td width="1%">
            <input type="radio" name="allowLog" value="true" id="not04"
             <%= ((allowLog) ? "checked" : "") %>>
            </td>
            <td width="99%">
                <label for="not02"><b>Allow</b></label> - Packets will be logged.
            </td>
        </tr>
        <tr>
	        <td>&nbsp;</td>
	        <td><input type="checkbox" name="logMonitorPacket" value="iq" <%= logIQEnabled ? "checked" : "" %>/>Enable IQ.
	            <input type="checkbox" name="logMonitorPacket" value="message" <%= logMessageEnabled ? "checked" : "" %>/>Enable Message.
	            <input type="checkbox" name="logMonitorPacket" value="presence" <%= logPresenceEnabled ? "checked" : "" %>/>Enable Presence.
	            <input type="checkbox" name="logMonitorPacket" value="other" <%= logOtherEnabled ? "checked" : "" %>/>Enable Other.
	        </td>
	    </tr>
        <tr>
            <td>&nbsp;</td>
            <td><input type="checkbox" name="logMonitorPacket" value="incoming" <%= logPacketIncoming ? "checked" : "" %>/>Enable log incoming packet.
                <input type="checkbox" name="logMonitorPacket" value="outter" <%= logPacketOutter ? "checked" : "" %>/>Enable log out put packet.
                <input type="checkbox" name="logMonitorPacket" value="processing" <%= logPacketProcessing ? "checked" : "" %>/>Enable log processing packet.
                <input type="checkbox" name="logMonitorPacket" value="processed" <%= logPacketProcessed ? "checked" : "" %>/>Enable log processed packet.
            </td>
        </tr>
    </tbody>
    </table>
    </div>
</fieldset>

<br><br>

<input type="submit" name="save" value="Save settings">
<input type="submit" name="reset" value="Restore factory settings*">
</form>

<br><br>

<em>*Restores the storm plugin to its factory state, you will lose all changes ever made to this plugin!</em>
</body>
</html>