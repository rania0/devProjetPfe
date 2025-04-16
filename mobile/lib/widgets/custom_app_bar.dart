import 'package:flutter/material.dart';
import '../services/auth/token_storage.dart';
import '../screens/auth/login_screen.dart';
import 'package:dio/dio.dart';
// Si tu veux ajouter appel API backend logout
// import 'package:dio/dio.dart';

class CustomAppBar extends StatelessWidget implements PreferredSizeWidget {
  final String nom;

  const CustomAppBar({super.key, required this.nom});

  void logout(BuildContext context) async {

    try {
      final token = await TokenStorage.getToken();
      await Dio().post(
        'http://localhost:9090/api/auth/logout',
        options: Options(headers: {'Authorization': 'Bearer $token'}),
      );
    } catch (e) {
      print("❌ Logout backend failed: $e");
    }


    // ✅ Suppression locale des tokens
    await TokenStorage.clear();

    // ✅ Redirection vers écran login
    Navigator.pushAndRemoveUntil(
      context,
      MaterialPageRoute(builder: (context) => const LoginScreen()),
          (route) => false,
    );
  }

  @override
  Widget build(BuildContext context) {
    return AppBar(
      backgroundColor: Colors.white,
      elevation: 2,
      automaticallyImplyLeading: false,
      title: Text(
        '👋 Bienvenue $nom',
        style: const TextStyle(
          color: Colors.black,
          fontWeight: FontWeight.bold,
        ),
      ),
      actions: [
        IconButton(
          icon: const Icon(Icons.logout, color: Colors.redAccent),
          tooltip: "Se déconnecter",
          onPressed: () => logout(context), // 💥 Ici on appelle la fonction
        )
      ],
    );
  }

  @override
  Size get preferredSize => const Size.fromHeight(kToolbarHeight);
}
