-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versione server:              10.1.34-MariaDB - mariadb.org binary distribution
-- S.O. server:                  Win32
-- HeidiSQL Versione:            9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dump della struttura del database cinema
CREATE DATABASE IF NOT EXISTS `cinema` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `cinema`;

-- Dump della struttura di tabella cinema.accounts
CREATE TABLE IF NOT EXISTS `accounts` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `Username` varchar(50) NOT NULL,
  `Password` varchar(100) NOT NULL,
  PRIMARY KEY (`Username`,`Password`),
  UNIQUE KEY `ID` (`ID`,`Username`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

-- Dump dei dati della tabella cinema.accounts: ~2 rows (circa)
/*!40000 ALTER TABLE `accounts` DISABLE KEYS */;
INSERT INTO `accounts` (`ID`, `Username`, `Password`) VALUES
	(1, 'Tizio', 'ea4af65fd1b4ab897843f0e772c3f74fe663d1ad60d197cd333ce551a066e5d3'),
	(2, 'a', 'd5a7a583052bc7c873b8516fd3f617e700ce83fb7ab954883edecef62c20d92e');
/*!40000 ALTER TABLE `accounts` ENABLE KEYS */;

-- Dump della struttura di tabella cinema.prenotazioni
CREATE TABLE IF NOT EXISTS `prenotazioni` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `Account` int(10) unsigned NOT NULL,
  `Spettacolo` int(10) unsigned NOT NULL,
  `Numero_posto` int(10) unsigned NOT NULL,
  `Data_prenotazione` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`Account`,`Spettacolo`,`Numero_posto`),
  UNIQUE KEY `ID` (`ID`),
  KEY `FK_Prenotazioni_spettacoli` (`Spettacolo`),
  CONSTRAINT `FK_Prenotazioni_accounts` FOREIGN KEY (`Account`) REFERENCES `accounts` (`ID`),
  CONSTRAINT `FK_Prenotazioni_spettacoli` FOREIGN KEY (`Spettacolo`) REFERENCES `spettacoli` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

-- Dump dei dati della tabella cinema.prenotazioni: ~0 rows (circa)
/*!40000 ALTER TABLE `prenotazioni` DISABLE KEYS */;
INSERT INTO `prenotazioni` (`ID`, `Account`, `Spettacolo`, `Numero_posto`, `Data_prenotazione`) VALUES
	(1, 1, 4, 25, '2018-11-17 10:29:27');
/*!40000 ALTER TABLE `prenotazioni` ENABLE KEYS */;

-- Dump della struttura di tabella cinema.sale
CREATE TABLE IF NOT EXISTS `sale` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `Nome` varchar(50) NOT NULL,
  `Posti` int(5) unsigned NOT NULL,
  PRIMARY KEY (`ID`,`Nome`),
  UNIQUE KEY `ID` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

-- Dump dei dati della tabella cinema.sale: ~0 rows (circa)
/*!40000 ALTER TABLE `sale` DISABLE KEYS */;
INSERT INTO `sale` (`ID`, `Nome`, `Posti`) VALUES
	(1, 'Boiler', 150);
/*!40000 ALTER TABLE `sale` ENABLE KEYS */;

-- Dump della struttura di tabella cinema.spettacoli
CREATE TABLE IF NOT EXISTS `spettacoli` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `Data` date NOT NULL,
  `Ora` time NOT NULL,
  `Titolo` varchar(100) NOT NULL,
  `Sala` int(10) unsigned NOT NULL,
  `Descrizione` varchar(200) DEFAULT '0',
  PRIMARY KEY (`Data`,`Titolo`,`Sala`),
  UNIQUE KEY `ID` (`ID`),
  KEY `FK_spettacoli_sale` (`Sala`),
  CONSTRAINT `FK_spettacoli_sale` FOREIGN KEY (`Sala`) REFERENCES `sale` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

-- Dump dei dati della tabella cinema.spettacoli: ~0 rows (circa)
/*!40000 ALTER TABLE `spettacoli` DISABLE KEYS */;
INSERT INTO `spettacoli` (`ID`, `Data`, `Ora`, `Titolo`, `Sala`, `Descrizione`) VALUES
	(4, '2018-11-17', '10:07:14', 'Trevo', 1, 'Martina');
/*!40000 ALTER TABLE `spettacoli` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
