# -*- coding: utf-8 -*-
from django.views.generic.base import TemplateView
from django.core.cache import cache
from django.shortcuts import redirect

class LoginView(TemplateView):

    template_name = "login.html"
    def dispatch(self, request, *args, **kwargs):
        #쿠키를 확인하여 로그인되어 있으면 매니지 화면으로 리다이렉트한다.

        login_cookie = request.COOKIES.get('login_cookie')
        if cache.get(login_cookie)=='admin':
            return redirect('/manage/')
        else:
            return super(LoginView, self).dispatch(request, *args, **kwargs)

class ManageView(TemplateView):
    
    template_name = "manage.html"
    def dispatch(self, request, *args, **kwargs):
        #쿠키를 확인하여 로그인되어 있지 않으면 로그인 화면으로 리다이렉트한다.
        login_cookie = request.COOKIES.get('login_cookie')
        if cache.get(login_cookie)=='admin':
            return super(ManageView, self).dispatch(request, *args, **kwargs)
        else:
            return redirect('/')

class MainView(TemplateView):

    template_name = "main.html"
    def get_context_data(self, **kwargs):
        context = super(MainView, self).get_context_data(**kwargs)
        return context