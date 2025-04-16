import 'package:flutter/material.dart';
import 'screens/auth/login_screen.dart';
import 'services/auth/token_storage.dart';
import 'screens/main_layout.dart';
import 'package:jwt_decoder/jwt_decoder.dart';

final GlobalKey<NavigatorState> navigatorKey = GlobalKey<NavigatorState>();

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  Future<Widget> _getInitialScreen() async {
    final token = await TokenStorage.getToken();
    final role = await TokenStorage.getRole();
    final nom = "Utilisateur"; // si tu veux le stocker aussi

    if (token != null && !JwtDecoder.isExpired(token)) {
      return MainLayout(nom: nom, role: role ?? '');
    } else {
      return const LoginScreen();
    }
  }

  @override
  Widget build(BuildContext context) {
    return FutureBuilder<Widget>(
      future: _getInitialScreen(),
      builder: (context, snapshot) {
        if (!snapshot.hasData) {
          return const MaterialApp(
            home: Scaffold(
              body: Center(child: CircularProgressIndicator()),
            ),
          );
        }

        return MaterialApp(
          navigatorKey: navigatorKey,
          debugShowCheckedModeBanner: false,
          title: 'DevProjetPfe',
          theme: ThemeData(primarySwatch: Colors.blue),
          home: snapshot.data!,
        );
      },
    );
  }
}
