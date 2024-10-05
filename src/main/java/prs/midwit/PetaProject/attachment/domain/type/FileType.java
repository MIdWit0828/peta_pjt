package prs.midwit.PetaProject.attachment.domain.type;

public enum FileType {
    APP, DOC, VID, IMG, PPT,ERR;


    public static FileType nameOf(String name) {
        switch (name) {
            case "app" -> {
                return FileType.APP;
            }
            case "doc" -> {
                return FileType.DOC;
            }
            case "vid" -> {
                return FileType.VID;
            }
            case "img" -> {
                return FileType.IMG;
            }
            case "ppt" -> {
                return FileType.PPT;
            }
            default -> {
                return FileType.ERR;
            }
        }
    }

}
