# Animal Marketplace

## Description
Animal Marketplace est une application web développée en Java avec Spring Boot. Elle permet aux utilisateurs de publier et de consulter des annonces d'animaux. L'application utilise Amazon Cognito pour l'authentification OAuth 2.0, Amazon S3 pour le stockage des fichiers, Amazon RDS pour la gestion de la base de données MySQL, AWS Elastic Beanstalk pour le déploiement, AWS Lambda pour envoyer un message de bienvenue aux utilisateurs enregistrés, et CodeGuru pour la revue de code.

## Fonctionnalités
- **Gestion des annonces** : Créer, lire, mettre à jour et supprimer des annonces.
- **Authentification sécurisée** : Intégration avec Amazon Cognito pour l'authentification OAuth 2.0.
- **Stockage des fichiers** : Utilisation d'Amazon S3 pour le stockage des images des annonces.
- **Base de données cloud** : Utilisation d'Amazon RDS pour la gestion de la base de données MySQL.
- **Déploiement automatisé** : Utilisation d'AWS Elastic Beanstalk pour le déploiement.
- **Pipeline CI/CD** : Intégration avec AWS CodePipeline et AWS CodeBuild.
- **Revue de code** : Analyse du code avec AWS CodeGuru.
- **Notification utilisateur** : Utilisation d'AWS Lambda pour envoyer un message de bienvenue aux utilisateurs enregistrés.

## Technologies utilisées
- **Langage** : Java
- **Framework** : Spring Boot
- **Base de données** : Amazon RDS (MySQL)
- **Build Tool** : Maven
- **Cloud Services** : Amazon Cognito, Amazon S3, Amazon RDS, AWS Elastic Beanstalk, AWS Lambda
- **CI/CD** : AWS CodePipeline, AWS CodeBuild
- **Revue de code** : AWS CodeGuru

## Prérequis
- **Java** : Version 17 ou supérieure
- **Maven** : Version 3.6 ou supérieure
- **AWS** : Compte AWS avec accès à Cognito, S3, RDS, Elastic Beanstalk, Lambda et CodeGuru

## Installation

### Étape 1 : Cloner le projet
```bash
git clone https://github.com/alycamara/animal-marketplace.git
cd animal-marketplace 
```

### Étape 2 : Configurer les variables d'environnement
Ajoutez les variables nécessaires dans un fichier .env ou configurez-les directement dans votre environnement :

- **DB_URL** : URL de la base de données.
- **DB_USERNAME** : Nom d'utilisateur de la base de données.
- **DB_PASSWORD** : Mot de passe de la base de données.
- **S3_BUCKET_NAME** : Nom du bucket S3.
- **REGION** : Région AWS.
- **ACCESS_KEY_ID** : Clé d'accès AWS.
- **SECRET_ACCESS_KEY** : Clé secrète AWS.
- **CLIENT_ID** : ID client Cognito.
- **CLIENT_SECRET** : Secret client Cognito.
- **COGNITO_REDIRECT_URI** : URI de redirection Cognito.
- **ISSUER_URI** : URI de l'émetteur Cognito.
- **COGNITO_DOMAIN** : Domaine Cognito.
- **LOGOUT_REDIRECT_URL** : URL de redirection après déconnexion.

### Étape 3 : Construire le projet
```bash
mvn clean install
```
### Étape 4 : Exécuter le projet
```bash
java -jar target/animal-marketplace.jar 
```
### Déploiement
Le projet utilise AWS CodePipeline et AWS CodeBuild pour l'intégration continue et le déploiement sur AWS Elastic Beanstalk. Le fichier buildspec.yml configure les étapes de construction, et le fichier .github/workflows/workflow.yml configure l'analyse du code avec CodeGuru Reviewer.

### Configuration
Les paramètres de l'application sont définis dans le fichier src/main/resources/application.properties. Vous pouvez les personnaliser selon vos besoins.

### Contribution
Les contributions sont les bienvenues ! Veuillez suivre les étapes ci-dessous :  
- Forkez le projet.
- Créez une branche pour vos modifications : git checkout -b feature/nom-de-la-fonctionnalite.
- Assurez-vous que votre code respecte les normes de style et passe les tests.
- Ajoutez des commentaires clairs et concis dans votre code.
- Incluez des tests unitaires pour toute nouvelle fonctionnalité ou correction de bug.
- Mettez à jour la documentation si nécessaire.
- Assurez-vous que le code est compatible avec les versions de Java et Maven spécifiées dans les prérequis.
- Vérifiez que toutes les dépendances sont correctement déclarées dans le fichier pom.xml.
- Assurez-vous que le projet compile et s'exécute correctement avant de soumettre votre pull request.

### Contact
Pour toute question ou suggestion, veuillez contacter :
- **Nom** : Aly Camara
- **Email** : camaraaly7@gmail.com
- **GitHub** : alycamara


