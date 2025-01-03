export class CommentStatus {
    commentId: number | null;
    content: string | null;

    constructor(commentId?: number, content?: string) {
        this.commentId = commentId || null;
        this.content = content || null;

    }
}