import { Role } from "./role";

export class User {
    
    userId: number | null;
    name: string | null;
    surname: string | null;
    email: string | null;
    username: string | null;
    password: string | null;
    role: Role | null;

    constructor(userId?: number, name?: string, surname?: string, email?: string, username?: string, password?: string, role?: Role) {
        this.userId = userId || null;
        this.name = name || null;
        this.surname = surname || null;
        this.email = email || null;
        this.username = username || null;
        this.password = password || null;
        this.role = role || null;
    }
}