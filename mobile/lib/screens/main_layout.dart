import 'package:flutter/material.dart';
import '../widgets/custom_app_bar.dart';
import '../widgets/custom_bottom_nav.dart';
import 'profile_screen.dart';
import 'responsable/responsable_home.dart';
import 'livreur/livreur_home.dart';

class MainLayout extends StatefulWidget {
  final String nom;
  final String role;

  const MainLayout({super.key, required this.nom, required this.role});

  @override
  State<MainLayout> createState() => _MainLayoutState();
}

class _MainLayoutState extends State<MainLayout> {
  int _selectedIndex = 0;

  List<Widget> getPagesByRole() {
    if (widget.role == 'responsable_point_vente') {
      return [
        const ResponsableHome(),
        ProfileScreen(),
      ];
    } else if (widget.role == 'livreur') {
      return [
        const LivreurHome(),
        ProfileScreen(),
      ];
    } else {
      return [
        const Center(child: Text("🚫 Rôle inconnu")),
        ProfileScreen(),
      ];
    }
  }

  @override
  Widget build(BuildContext context) {
    final pages = getPagesByRole();

    return Scaffold(
      appBar: CustomAppBar(nom: widget.nom),
      body: pages[_selectedIndex],
      bottomNavigationBar: CustomBottomNavBar(
        currentIndex: _selectedIndex,
        onTap: (index) {
          setState(() {
            _selectedIndex = index;
          });
        },
      ),
    );
  }
}
