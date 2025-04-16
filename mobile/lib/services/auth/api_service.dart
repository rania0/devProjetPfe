import 'package:dio/dio.dart';
import '../../models/auth/login_response.dart';
import '../../utils/constants.dart';
import 'auth_interceptor.dart';

class ApiService {
  final Dio dio;

  ApiService()
      : dio = Dio(BaseOptions(
    baseUrl: baseUrl,
    headers: {
      'Content-Type': 'application/json',
    },
  )) {
    dio.interceptors.add(AuthInterceptor(dio: dio));
  }

  Future<LoginResponse> login(String email, String password) async {
    final response = await dio.post('/api/auth/login', data: {
      'mail': email,
      'password': password,
    });

    print("📥 Données backend : ${response.data}");
    return LoginResponse.fromJson(response.data);
  }

  // 🔁 Exemple de requête GET sécurisée avec token auto
  Future<Response> getSecuredData() async {
    return await dio.get('/api/secured/data');
  }
}
