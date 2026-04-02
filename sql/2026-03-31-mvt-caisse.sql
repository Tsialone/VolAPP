CREATE TABLE caisse_mvt(
   id_cm SERIAL,
   debit DOUBLE PRECISION NOT NULL,
   credit DOUBLE PRECISION NOT NULL,
   created TIMESTAMP NOT NULL,
   id_depense INTEGER,
   id_caisse INTEGER NOT NULL,
   PRIMARY KEY(id_cm),
   FOREIGN KEY(id_depense) REFERENCES depense(id_depense),
   FOREIGN KEY(id_caisse) REFERENCES caisse(id_caisse)
);


CREATE TABLE caisse(
   id_caisse SERIAL,
   nom VARCHAR(255)  NOT NULL,
   description TEXT,
   created TIMESTAMP NOT NULL,
   solde  DOUBLE PRECISION NOT NULL,
   id_caisse_categorie INTEGER NOT NULL,
   id_utilisateur INTEGER NOT NULL,
   PRIMARY KEY(id_caisse),
   UNIQUE(id_caisse_categorie),
   FOREIGN KEY(id_caisse_categorie) REFERENCES caisse_categorie(id_caisse_categorie),
   FOREIGN KEY(id_utilisateur) REFERENCES utilisateur(id_utilisateur)
);
INSERT INTO caisse (nom, description, created, solde, id_caisse_categorie, id_utilisateur) 
VALUES ('portefeuille', 'mlay e', NOW  () , 0.0 , 1 , 1);

ALTER TABLE depense ADD COLUMN montant_total  DOUBLE PRECISION  NOT NULL  DEFAULT 0.0;
