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


-- Dump della struttura del database teatro
CREATE DATABASE IF NOT EXISTS `teatro` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `teatro`;

-- Dump della struttura di tabella teatro.accounts
CREATE TABLE IF NOT EXISTS `accounts` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `Username` varchar(50) NOT NULL,
  `Password` varchar(50) NOT NULL,
  PRIMARY KEY (`Username`,`Password`),
  UNIQUE KEY `ID` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dump dei dati della tabella teatro.accounts: ~0 rows (circa)
/*!40000 ALTER TABLE `accounts` DISABLE KEYS */;
/*!40000 ALTER TABLE `accounts` ENABLE KEYS */;

-- Dump della struttura di tabella teatro.prenotazioni
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dump dei dati della tabella teatro.prenotazioni: ~0 rows (circa)
/*!40000 ALTER TABLE `prenotazioni` DISABLE KEYS */;
/*!40000 ALTER TABLE `prenotazioni` ENABLE KEYS */;

-- Dump della struttura di tabella teatro.spettacoli
CREATE TABLE IF NOT EXISTS `spettacoli` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `Data` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `Titolo` varchar(100) NOT NULL,
  `Sala` int(3) unsigned NOT NULL,
  `Descrizione` varchar(200) DEFAULT '0',
  PRIMARY KEY (`Data`,`Titolo`,`Sala`),
  UNIQUE KEY `ID` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dump dei dati della tabella teatro.spettacoli: ~0 rows (circa)
/*!40000 ALTER TABLE `spettacoli` DISABLE KEYS */;
/*!40000 ALTER TABLE `spettacoli` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
