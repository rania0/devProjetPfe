import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import '../../main.dart';
import 'token_storage.dart';
import '../../screens/auth/login_screen.dart';

class AuthInterceptor extends Interceptor {
  final Dio dio;

  AuthInterceptor({required this.dio});

  @override
  void onRequest(RequestOptions options, RequestInterceptorHandler handler) async {
    final token = await TokenStorage.getToken();
    if (token != null) {
      options.headers['Authorization'] = 'Bearer $token';
    }
    return handler.next(options);
  }

  @override
  void onError(DioException err, ErrorInterceptorHandler handler) async {
    if (err.response?.statusCode == 401) {
      final refreshToken = await TokenStorage.getRefreshToken();

      if (refreshToken != null) {
        try {
          final response = await dio.post(
            '/api/auth/refresh',
            data: {'refreshToken': refreshToken},
          );

          final newAccessToken = response.data['token'];
          final newRefreshToken = response.data['refreshToken'];
          final role = await TokenStorage.getRole();

          // 🔁 Save new tokens
          await TokenStorage.saveTokens(newAccessToken, newRefreshToken, role ?? '');

          // 🔁 Retry failed request with new token
          final clonedRequest = err.requestOptions;
          clonedRequest.headers['Authorization'] = 'Bearer $newAccessToken';

          final retryResponse = await dio.fetch(clonedRequest);
          return handler.resolve(retryResponse);
        } catch (e) {
          print("❌ Refresh failed: $e");

          // ❌ Failed refresh → logout
          await TokenStorage.clear();

          // optional: redirect user
          navigatorKey.currentState?.pushAndRemoveUntil(
            MaterialPageRoute(builder: (context) => const LoginScreen()),
                (route) => false,
          );
        }
      }
    }

    return handler.next(err);
  }
}
