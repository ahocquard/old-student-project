Java


Le programme comporte 5 arguments :
- no de la mesure de dissimilarité (jaccard ou cosinus)
- format de sortie : R ou orange
- colonne ou il y a le concept (car les premiers fichiers comportaient une première colonne qui 
indiquait dans quelle catégorie le concept était indexé, et dan sla deuxième le concept)
- argument qui indique si on prend en compte les null, c'était pour pondérer,
mais ça n'a pas été implémenté, donc argument inutile
- nom du fichier de la liste des concepts retenus dans la matrice (1 mot par ligne de ce fichier),
si l'argurment n'est pas indiqué, prend tous les mots (c'était pour filtrer les petits clusters)


- Le programme peut être importer dans Eclipse et exécuter simplement (penser à mettre les arguments => Run Configurations)



Améliorations possibles :
- nom du fichier d'entré en argument et non dans une variable (DaoConcept.CORPUS_FILE_NAME)
- Clustering par seuil (voisinage) en option dans les paramètres (ça le fait automatiquement pour le momment)
- résultat du clustering par seuil dans un dossier selon la mesure de dissimilarité (/jaccard/...)




