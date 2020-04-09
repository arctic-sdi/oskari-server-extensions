<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
    <title>${viewName}</title>
    <link rel="shortcut icon" href="/Oskari${path}/css/asdi_logoplugin_logo.png" type="image/png" />
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    <!-- IE 9 polyfill for openlayers 3 - https://github.com/openlayers/ol3/issues/4865 -->
    <!--[if lte IE 9]> <script src="https://cdn.polyfill.io/v2/polyfill.min.js?features=fetch,requestAnimationFrame,Element.prototype.classList"></script> <![endif]-->

    <!-- ############# css ################# -->
    <link
            rel="stylesheet"
            type="text/css"
            href="/Oskari${path}/icons.css"/>

    <link
            rel="stylesheet"
            type="text/css"
            href="/Oskari${path}/oskari.min.css"/>

    <link href="https://fonts.googleapis.com/css?family=Noto+Sans" rel="stylesheet">
    <style type="text/css">
        @media screen {
            body {
                margin : 0;
                padding : 0;
            }
            #mapdiv {
                width: 100%;
            }
            #contentMap {
                height: 100%;
            }
        }
    </style>
    <!-- ############# /css ################# -->
</head>
<body>
<div id="contentMap" class="oskariui container-fluid published">
    <div class="row-fluid" style="height: 100%; background-color:white;">
        <div class="oskariui-left"></div>
        <div class="span12 oskariui-center" style="height: 100%; margin: 0;">
            <div id="mapdiv"></div>
        </div>
        <div class="oskari-closed oskariui-right">
            <div id="mapdivB"></div>
        </div>
    </div>
</div>


<!-- ############# Javascript ################# -->

<!--  OSKARI -->

<script type="text/javascript">
    var ajaxUrl = '${ajaxUrl}';
    var controlParams = ${controlParams};
</script>
<%-- Pre-compiled application JS, empty unless created by build job --%>
<script type="text/javascript"
        src="/Oskari${path}/oskari.min.js">
</script>

<%-- language files --%>
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
