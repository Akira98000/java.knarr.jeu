import json
import os
import sys
import argparse
from datetime import datetime

class JsonAnalyzer:
    def __init__(self, file_path):
        self.file_path = file_path
        self.data = self.load_json()
        
    def load_json(self):
        if not os.path.isfile(self.file_path):
            raise FileNotFoundError(f"Le fichier JSON '{self.file_path}' n'a pas été trouvé.")
        try:
            with open(self.file_path, 'r', encoding='utf-8') as f:
                return json.load(f)
        except json.JSONDecodeError as e:
            raise ValueError(f"Erreur lors de la lecture du fichier JSON : {e}")

    def get_average_points(self, bot_type):
        classement = self.data.get("Classement")
        if not classement:
            raise ValueError("JSON invalide : Clé 'Classement' manquante.")

        total_point_renommer = 0
        total_point_victoire = 0
        count = 0

        for partie_name, partie in classement.items():
            for joueur_name, joueur in partie.items():
                type_bot = joueur.get("Type")
                if type_bot == bot_type:
                    point_renommer = joueur.get("PointRenommer", 0)
                    point_victoire = joueur.get("PointVictoire", 0)
                    total_point_renommer += point_renommer
                    total_point_victoire += point_victoire
                    count += 1

        if count == 0:
            return (0.0, 0.0)

        average_point_renommer = total_point_renommer / count
        average_point_victoire = total_point_victoire / count

        return (average_point_renommer, average_point_victoire)

    def get_game_count(self):
        classement = self.data.get("Classement")
        if not classement:
            return 0
        return len(classement)

def find_latest_json():
    files = [f for f in os.listdir('.') if f.startswith('partieKnarr_') and f.endswith('.json')]
    if not files:
        raise FileNotFoundError("Aucun fichier JSON correspondant au pattern trouvé.")
    return max(files)

def extract_datetime_from_filename(filename):
    try:
        date_time_str = filename.split('_')[2:4]
        date_str, time_str = date_time_str[0], date_time_str[1].split('.')[0]
        
        full_str = f"{date_str}_{time_str}"
        dt = datetime.strptime(full_str, "%Y%m%d_%H%M%S")
        
        return dt.strftime("%d/%m/%Y à %H:%M:%S")
    except:
        return "Date non disponible"

def main():
    parser = argparse.ArgumentParser(description='Analyse des fichiers JSON de parties')
    group = parser.add_mutually_exclusive_group(required=True)
    group.add_argument('file', nargs='?', help='Chemin du fichier JSON')
    group.add_argument('-l', '--last', action='store_true', help='Utiliser le dernier fichier JSON')
    
    args = parser.parse_args()

    try:
        if args.last:
            file_path = find_latest_json()
        else:
            file_path = args.file

        print(f"\nAnalyse du fichier : {file_path}")
        print(f"Date de la partie : {extract_datetime_from_filename(file_path)}")

        analyzer = JsonAnalyzer(file_path)
        print(f"Nombre de parties : {analyzer.get_game_count()}\n")

        for bot_type in ["BOTA", "BOTB", "BOTC", "BOTD"]:
            averages = analyzer.get_average_points(bot_type)
            print(f"Moyenne pour {bot_type} :")
            print(f"PointRenommer : {averages[0]:.2f}")
            print(f"PointVictoire : {averages[1]:.2f}\n")

    except FileNotFoundError as fnf_error:
        print(fnf_error)
    except ValueError as val_error:
        print(val_error)
    except Exception as e:
        print(f"Une erreur inattendue s'est produite : {e}")

if __name__ == "__main__":
    main()