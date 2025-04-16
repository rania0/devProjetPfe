class LoginResponse {
  final String token;
  final String refreshToken;
  final String role;
  final String nom;

  LoginResponse({
    required this.token,
    required this.refreshToken,
    required this.role,
    required this.nom,
  });

  factory LoginResponse.fromJson(Map<String, dynamic> json) {
    // ✅ Vérification stricte – tokens ne doivent jamais être null
    if (json['token'] == null || json['refreshToken'] == null) {
      throw Exception("❌ Token ou RefreshToken manquant !");
    }

    return LoginResponse(
      token: json['token'],
      refreshToken: json['refreshToken'],
      role: json['role'] ?? '',
      nom: json['nom'] ?? '',
    );
  }
}
