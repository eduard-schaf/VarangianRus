package main;

/**
 * @author Eduard Schaf
 * @since 30.01.17
 */
public class Slash {
    private String targetId;
    private String relation;

    public Slash(String targetId, String relation) {
        this.targetId = targetId;
        this.relation = relation;
    }

    public String getTargetId() {
        return targetId;
    }

    public String getRelation() {
        return relation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Slash slash = (Slash) o;

        if (targetId != null ? !targetId.equals(slash.targetId) : slash.targetId != null) {
            return false;
        }
        return relation != null ? relation.equals(slash.relation) : slash.relation == null;
    }

    @Override
    public int hashCode() {
        int result = targetId != null ? targetId.hashCode() : 0;
        result = 31 * result + (relation != null ? relation.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Slash{" +
                "targetId='" + targetId + '\'' +
                ", relation='" + relation + '\'' +
                '}';
    }
}
