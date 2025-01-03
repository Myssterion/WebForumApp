export class Topic {
    id: number | null;
    topicName: string | null;

    constructor(id?: number, topicName?: string) {
        this.id = id || null;
        this.topicName = topicName || null;
    }
}