<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
    <title>Arctic SDI - ${viewName}</title>
    <meta charset="utf-8"/>
    <!-- ############# css ################# -->
    <link type="text/css" rel="stylesheet"
          href="//fonts.googleapis.com/css?family=Open+Sans:400,400italic,700,700italic,800,800italic,600italic,600"/>
    <link
            rel="stylesheet"
            type="text/css"
            href="/Oskari${path}/icons.css"/>
    <link
            rel="stylesheet"
            type="text/css"
            href="/Oskari${path}/oskari.min.css"/>

    <style type="text/css">
        @media screen {
            #login {
                margin-left: 5px;
                margin-right: 5px;
                margin-top: 20px;
            }

            #login input[type="text"], #login input[type="password"] {
                width: 90%;
                margin-bottom: 5px;
                background-image: url("/Oskari/${version}/resources/images/forms/input_shadow.png");
                background-repeat: no-repeat;
                padding-left: 5px;
                padding-right: 5px;
                border: 1px solid #B7B7B7;
                border-radius: 4px 4px 4px 4px;
                box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1) inset;
                color: #878787;
                font: 13px/100% Arial, sans-serif;
            }

            #login input[type="submit"] {
                width: 100%;
                margin-bottom: 5px;
                padding-left: 5px;
                padding-right: 5px;
                border: 1px solid #B7B7B7;
                border-radius: 4px 4px 4px 4px;
                box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1) inset;
                color: #878787;
                font: 13px/100% Arial, sans-serif;
            }

            #login p.error {
                font-weight: bold;
                color: red;
                margin-bottom: 10px;
            }

            #login div.link {
                padding: 5px;
            }

            #language-selector-root {
                padding: 10px 5px 0 5px;
            }
        }
    </style>
    <!-- ############# /css ################# -->
</head>
<body>

<nav id="maptools">
    <div id="logobar">
    </div>
    <div id="langSelector">
    </div>
    <div id="menubar">
    </div>
    <div id="divider">
    </div>
    <div id="toolbar">
    </div>
    <div id="login">
        <c:choose>
            <c:when test="${!empty loginState}">
                <p class="error"><spring:message code="invalid_password_or_username"
                                                 text="Invalid password or username!"/></p>
            </c:when>
        </c:choose>
        <c:choose>
            <%-- If logout url is present - so logout link --%>
            <c:when test="${!empty _logout_uri}">
                <c:if test="${!empty _registration_uri}">
                    <div class="link">
                        <a id="userRegistration"
                           href="${pageContext.request.contextPath}${_registration_uri}"><spring:message code="account"
                                                                                                         text="Account"/></a>
                    </div>
                </c:if>
                <div class="link">
                    <form action="${pageContext.request.contextPath}${_logout_uri}" method="POST" id="logoutform">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <a href="${pageContext.request.contextPath}${_logout_uri}" onClick="jQuery('#logoutform').submit();return false;"><spring:message code="logout" text="Logout" /></a>
                    </form>
                </div>
            </c:when>
            <%-- Otherwise show appropriate logins --%>
            <c:otherwise>
                <c:if test="${!empty _registration_uri}">
                    <div class="link">
                        <a href="${pageContext.request.contextPath}${_registration_uri}"><spring:message
                                code="user.registration" text="Register"/></a>
                    </div>
                </c:if>
                <c:if test="${!empty _login_uri_saml}">
                    <div class="link">
                        <a href="${pageContext.request.contextPath}${_login_uri_saml}"><spring:message code="login.sso"
                                                                                                       text="SSO login"/></a>
                        <hr/>
                    </div>
                </c:if>
            </c:otherwise>
        </c:choose>
    </div>
    <div id="language-selector-root"></div>
</nav>
<%-- Oskari will create the map container here --%>

<!-- ############# Javascript ################# -->

<!--  OSKARI -->

<script type="text/javascript">
    var ajaxUrl = '${ajaxUrl}';
    var language = '${language}';
    var controlParams = ${controlParams};
</script>

<script type="text/javascript"
        src="/Oskari${path}/oskari.min.js">
</script>
<%--language files --%>
<script type="text/javascript"
        src="/Oskari${path}/oskari_lang_${language}.js">
</script>

<script type="text/javascript"
        src="/Oskari${path}/index.js">
</script>

<% pageContext.setAttribute("hasTracking", !"test".equals(request.getParameter("tracking"))); %>
<c:if test="${hasTracking}">
<!-- Matomo -->
<script type="text/javascript">
    var _paq = _paq || [];
    _paq.push(['enableLinkTracking']);
    _paq.push(['setDocumentTitle', document.domain + "/${appsetupUUID}"]);
    _paq.push(['setCustomVariable', 1, "Type", "${appsetupType}", "page"]);
    _paq.push(['setCustomVariable', 2, "Domain", document.domain, "page"]);
    /* tracker methods like "setCustomDimension" should be called before "trackPageView" */
    _paq.push(['trackPageView']);
    (function() {
        var u="https://matomo.nls.fi/";
        _paq.push(['setTrackerUrl', u+'matomo.php']);
        _paq.push(['setSiteId', '14']);
        var d=document, g=d.createElement('script'), s=d.getElementsByTagName('script')[0];
        g.type='text/javascript'; g.async=true; g.defer=true; g.src=u+'matomo.js'; s.parentNode.insertBefore(g,s);
    })();


    function _pushEvent  (/* variadic */) {
        var args = Array.prototype.slice.call(arguments);
        args.unshift('trackEvent');
        _paq.push(args);
    }

    jQuery('#maptools').on('click', '#toolbar .toolrow .tool', function () {
        var el = jQuery(this);
        if (el.hasClass('disabled')) {
            return;
        }
        _pushEvent('Toolbar', el.attr('tool'));
    });
    jQuery('#mapdiv .mapplugin.mylocationplugin').on('click', function () {
        _pushEvent('Maptools', 'mylocationtool');
    });
    jQuery('#mapdiv .mapplugin.coordinatetool').on('click', function () {
        _pushEvent('Maptools', 'coordinatetool');
    });

    var eventHandlers = {
        'userinterface.ExtensionUpdatedEvent': function (event) {
            if (event.getViewState() === 'attach') {
                _pushEvent('Tile', event.getExtension().getName());
            }
        },
        'SearchResultEvent': function (event) {
            var resultSize = event.getSuccess() ? event.getResult().totalCount || 0 : 0;
            var params = event.getRequestParameters();
            if (typeof params === 'object') {
                params = params.searchKey;
            }
            _paq.push(['trackSiteSearch',
                // Search keyword searched for
                params,
                // Search category selected in your search engine. If you do not need this, set to false
                false,
                // Number of results on the Search results page. Zero indicates a 'No Result Search Keyword'. Set to false if you don't know
                resultSize
            ]);
        }
    };
    var fakeBundle = {
        getName: function() {return "Telemetry"},
        onEvent: function(event) {
            var handler = eventHandlers[event.getName()];
            if (handler) {
                handler(event);
            }
        }
    };

    var _sandbox = Oskari.getSandbox();
    Object.keys(eventHandlers).forEach( function (name) {
        _sandbox.registerForEventByName(fakeBundle, name);
    });

</script>
<!-- End Matomo Code -->
</c:if>

<!-- ############# /Javascript ################# -->
</body>
</html>
