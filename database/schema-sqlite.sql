CREATE TABLE IF NOT EXISTS Artist (
    id_artist INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    birth_date TEXT,
    birth_place TEXT,
    nationality TEXT,
    photo TEXT
);

CREATE TABLE IF NOT EXISTS Artwork (
    id_artwork INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    id_artist INTEGER,
    artwork_type TEXT,
    price REAL,
    creation_year INTEGER,
    FOREIGN KEY (id_artist) REFERENCES Artist(id_artist) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Artwork_Images (
    id_image INTEGER PRIMARY KEY AUTOINCREMENT,
    id_artwork INTEGER,
    image_path TEXT,
    FOREIGN KEY (id_artwork) REFERENCES Artwork(id_artwork) ON DELETE CASCADE
);