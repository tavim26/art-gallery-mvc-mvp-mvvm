CREATE TABLE Artist (
    id_artist INT PRIMARY KEY IDENTITY(1,1),
    name NVARCHAR(100) NOT NULL,
    birth_date DATETIME,
    birth_place NVARCHAR(100),
    nationality NVARCHAR(50),
    photo NVARCHAR(255)
);

CREATE TABLE Artwork (
    id_artwork INT PRIMARY KEY IDENTITY(1,1),
    title NVARCHAR(100) NOT NULL,
    id_artist INT,
    artwork_type NVARCHAR(50),
    price DECIMAL(15, 2),
    creation_year INT,
    FOREIGN KEY (id_artist) REFERENCES Artist(id_artist) ON DELETE CASCADE
);

CREATE TABLE Artwork_Images (
    id_image INT PRIMARY KEY IDENTITY(1,1),
    id_artwork INT,
    image_path NVARCHAR(255),
    FOREIGN KEY (id_artwork) REFERENCES Artwork(id_artwork) ON DELETE CASCADE
);