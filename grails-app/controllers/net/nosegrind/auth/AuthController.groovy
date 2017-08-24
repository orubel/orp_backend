package net.nosegrind.auth


import main.groovy.net.nosegrind.auth.AuthInfo;
import net.nosegrind.auth.GrailsOAuthService;
import net.nosegrind.security.SpringSecuritySigninService;
import net.nosegrind.apiframework.Person

class AuthController {

    SpringSecuritySigninService springSecuritySigninService

    def signin(){
        GrailsOAuthService service = resolveService(params.provider)
        if (!service) {
            redirect(url: '/')
        }

        session["${params.provider}_originalUrl"] = params.originalUrl

        def callbackParams = [provider: params.provider]
        def callback = "${createLink(action: 'callback', absolute: 'true', params: callbackParams)}"
        def authInfo = service.getAuthInfo(callback)
        session["${params.provider}_authInfo"] = authInfo

        redirect(url: authInfo.authUrl)
    }

    def callback(){
		println("#### CALLBACK")
        GrailsOAuthService service = resolveService(params.provider)
        if (!service) {
            redirect(url: '/')
        }

        AuthInfo authInfo = session["${params.provider}_authInfo"]
        def requestToken = authInfo.requestToken
		println("### request : ${requestToken}")
        def accessToken = service.getAccessToken(authInfo.service, params, requestToken)
		println("### access : ${accessToken}")
        session["${params.provider}_authToken"] = accessToken

		def profile = service.getProfile(authInfo.service, accessToken)
        session["${params.provider}_profile"] = profile

        def uid = profile.uid
        Person user = Person.findByOauthIdAndOauthProvider(uid, params.provider)

        if (user) {
            springSecuritySigninService.signIn(user)
            redirect(uri: (session["${params.provider}_originalUrl"] ?: '/') - request.contextPath)
        } else {
            redirect(controller: 'user', action: 'register', params: params)
        }
    }

    private def resolveService(provider) {
        def serviceName = "${ provider as String }AuthService"
        grailsApplication.mainContext.getBean(serviceName)
    }

}
