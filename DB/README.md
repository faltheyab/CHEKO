# CHEKO Database:

High-level overview of the CHEKO restaurant management system database.

## Database Schema

![Database Schema](Schema%20screenshot.png)

## Core Technologies

- **PostgreSQL 13+**
- **PostGIS** for location-based services

## Key Features

### 1. Relational Schema Design

- Normalized database structure
- Proper entity relationships
- Referential integrity constraints
- Optimized for restaurant operations

### 2. Location Support

- Geospatial data types for restaurant locations


### 3. Performance Optimization

- Appropriate indexes for common queries
- Optimized table structures
- Query performance considerations
- Scalability planning

## Setup Instructions

The database can be set up using the following scripts:

- `Schema.sql` - Creates the database schema
- `SettingUp.sql` - Initial setup and configuration
- `Data.sql` - Sample data for development and testing

## Entity Relationships

- Branches have multiple menu sections
- Menu sections contain menu items
- Customers place orders
- Orders contain order items
- Order items reference menu items
- Orders have status tracking