export class Comment {
    commentId: number | null;
    content: string | null;
    username: string | null;
    posted: Date | null;

    constructor(commentId?: number, content?: string, posted?: Date, username?: string) {
        this.commentId = commentId || null;
        this.content = content || null;
        this.username = username || null;
        this.posted = posted || null;
    }
}