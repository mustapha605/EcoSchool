# Tracking / Eco-Data service

Service Node.js chargé des preuves mesurables : déchets recyclés (`WASTE_KG`), énergie économisée (`ENERGY_KWH`) et eau préservée (`WATER_LITERS`). Chaque action est enregistrée dans MongoDB puis publiée dans RabbitMQ sur `eco.events` avec la clé `tracking.action.recorded`.

`POST /api/tracking/actions` body example: `{ "classId":"6A", "metric":"WASTE_KG", "value":12.5 }`.
