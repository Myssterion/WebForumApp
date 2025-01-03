export class Permission {
    topicName: string;
    permissions: string[];

    constructor(topicName: string, permissions?: string[]) {
        this.topicName = topicName;
        this.permissions = permissions || [];
    }
}