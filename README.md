# Guía de Instalación y Configuración de MySQL y Beekeeper Studio

Esta guía te ayudará a instalar y configurar MySQL en Docker y Beekeeper Studio en tu sistema.

## Instalación

### 1. Instalar la imagen de MySQL en Docker

Para instalar la imagen de MySQL en Docker, ejecuta el siguiente comando en tu terminal:

```bash
docker pull mysql
```

### 2. Crear el contenedor de la imagen en Docker

Una vez que hayas descargado la imagen de MySQL, puedes crear un contenedor ejecutando el siguiente comando:

```bash
docker run --name MySQL_Container -e MYSQL_ROOT_PASSWORD=admin123 -p 3306:3306 -d mysql:latest
```

Este comando creará un contenedor de MySQL llamado "MySQL_Container" con la contraseña "admin123" para el usuario root y expondrá el puerto 3306.

## Descargar e Instalar Beekeeper Studio

Para descargar e instalar Beekeeper Studio, sigue estos pasos:

### 1. Descargar Beekeeper Studio

Descarga el instalador de Beekeeper Studio desde [este enlace](https://github.com/beekeeper-studio/beekeeper-studio/releases/tag/v4.3.1). Por ejemplo, puedes descargar "Beekeeper-Studio-Setup-4.3.1.exe".

### 2. Instalar Beekeeper Studio

Ejecuta el instalador descargado y sigue las instrucciones en pantalla para completar la instalación de Beekeeper Studio en tu sistema.

## Realizar el Test y Configuración de la Base de Datos

Una vez que hayas instalado MySQL en Docker y Beekeeper Studio en tu sistema, realiza el test y configura la base de datos siguiendo estos pasos:

1. Abre Beekeeper Studio.

2. Conéctate a tu base de datos MySQL utilizando las siguientes credenciales:
   - Usuario: root
   - Contraseña: admin123

   > **Nota**: Si todo va bien, el test deberá mostrar un mensaje de confirmación. Procede al siguiente paso.

3. Ahora con las mismas credenciales, haz clic en "Connect".

4. Ingresa el siguiente Script y ejecútalo:

```sql
CREATE USER 'redis'@'localhost' IDENTIFIED BY 'redis';
CREATE USER 'redis'@'%' IDENTIFIED BY 'redis';
GRANT ALL PRIVILEGES ON *.* TO 'redis'@'localhost' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON *.* TO 'redis'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;
```

   > **Nota**: Una vez que hayas ejecutado con éxito el script SQL, la configuración del usuario `redis` deberá estar completa.

   > **Importante**: Ahora las credenciales cambiaron, tanto el usuario como la contraseña son "redis".

## Descargar el Script SQL

1. Descarga el script SQL desde [este enlace](https://github.com/datacharmer/test_db).

2. Descomprime el archivo y verifica la ruta de guardado.

## Copia de Archivos al Contenedor

1. Copia los archivos y pégalos en el contenedor ejecutando el siguiente comando en tu terminal:

```bash
docker cp Ruta/de/la/carpeta/test_db-master MySQL_Container:/home/
```

2. Abre una sesión dentro del contenedor ejecutando el siguiente comando en tu terminal:

```bash
docker exec -it MySQL_Container bash
```

3. Dirígete al directorio dentro del contenedor donde copiaste la carpeta ejecutando el siguiente comando en el contenedor:

```bash
cd /home/test_db-master
```

4. Ejecuta el Script SQL "employees.sql" dentro del contenedor utilizando el siguiente comando en el contenedor:

```bash
mysql -u root -p < employees.sql
```

   > **Nota**: El password será `redis`.