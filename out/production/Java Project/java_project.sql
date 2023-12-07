-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : dim. 19 nov. 2023 à 23:54
-- Version du serveur : 10.4.27-MariaDB
-- Version de PHP : 8.2.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `java_project`
--

-- --------------------------------------------------------

--
-- Structure de la table `avis_citoyen`
--

CREATE TABLE `avis_citoyen` (
  `id_avis` int(11) NOT NULL,
  `rate` int(11) NOT NULL,
  `Time` date NOT NULL,
  `Type` varchar(255) DEFAULT NULL,
  `security` int(11) NOT NULL,
  `comment` varchar(255) NOT NULL,
  `id_citoyen` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `avis_citoyen`
--

INSERT INTO `avis_citoyen` (`id_avis`, `rate`, `Time`, `Type`, `security`, `comment`, `id_citoyen`) VALUES
(3, 4, '2023-11-16', 'SOLO', 4, 'ffffffffff', 5),
(5, 4, '2023-11-23', 'Famille', 3, 'aaaaaaa', 6),
(6, 4, '2023-11-14', 'Couple', 4, 'dffff', 7),
(7, 4, '2023-11-15', 'SOLO', 4, 'hhhh', 8);

-- --------------------------------------------------------

--
-- Structure de la table `citoyen`
--

CREATE TABLE `citoyen` (
  `ID` int(11) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `citoyen`
--

INSERT INTO `citoyen` (`ID`, `username`, `password`) VALUES
(1, 'ibrahim', '123456'),
(3, 'abc', '123'),
(4, 'ahmed', '123'),
(5, 'aaa', '1747'),
(6, 'ok', '124'),
(7, 'hhhh', '741258'),
(8, 'hhh', '789');

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `avis_citoyen`
--
ALTER TABLE `avis_citoyen`
  ADD PRIMARY KEY (`id_avis`),
  ADD KEY `id_citoyen` (`id_citoyen`);

--
-- Index pour la table `citoyen`
--
ALTER TABLE `citoyen`
  ADD PRIMARY KEY (`ID`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `avis_citoyen`
--
ALTER TABLE `avis_citoyen`
  MODIFY `id_avis` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT pour la table `citoyen`
--
ALTER TABLE `citoyen`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `avis_citoyen`
--
ALTER TABLE `avis_citoyen`
  ADD CONSTRAINT `avis_citoyen_ibfk_1` FOREIGN KEY (`id_citoyen`) REFERENCES `citoyen` (`ID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
