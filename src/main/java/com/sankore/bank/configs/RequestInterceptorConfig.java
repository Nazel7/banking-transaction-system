//package com.sankore.bank.configs;
//
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//
//@Configuration
//public class RequestInterceptorConfig extends ThreadPoolTaskExecutor {
//
//    public void requestInterceptor(final Runnable r) {
//
//        final Authentication a = SecurityContextHolder.getContext().getAuthentication();
//
//        super.execute(new Runnable() {
//            public void run() {
//                try {
//                    SecurityContext ctx = SecurityContextHolder.createEmptyContext();
//                    ctx.setAuthentication(a);
//                    SecurityContextHolder.setContext(ctx);
//                    r.run();
//                } finally {
//                    SecurityContextHolder.clearContext();
//                }
//            }
//        });
//    }
//}