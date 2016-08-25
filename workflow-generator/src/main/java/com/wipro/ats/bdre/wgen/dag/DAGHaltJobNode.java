package com.wipro.ats.bdre.wgen.dag;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by SU324335 on 7/1/16.
 */
public class DAGHaltJobNode extends  DAGNode {
    public DAGHaltJobNode() {

    }

    public String getName() {
        return "dag-halt-job";
    }

    @Override
    public String getDAG() {
        String homeDir = System.getProperty("user.home");
        String jobInfoFile = homeDir+"/bdre/airflow/"+getId().toString()+"_jobInfo.txt";
        try {

            FileWriter fw = new FileWriter(homeDir + "/defFile.txt", true);
            fw.write("\nf_" + getName().replace('-', '_') + "()");
            fw.close();
        }
        catch (IOException e){
            System.out.println("e = " + e);
        }
        return "with open('"+jobInfoFile+"','a+') as propeties_file:\n"+
                "\tfor line in propeties_file:\n"+
                "\t\tinfo = line.split('::',2)\n"+
                "\t\tdict[info[0]] = info[1].replace('\\n','')\n"+

                "\ndef "+getName().replace('-','_')+"_pc():\n" +
                "\tcommand='java -cp "+homeDir+"/bdre/lib/md_api/md_api-1.1-SNAPSHOT-executable.jar:"+homeDir+"/bdre/lib/*/*  com.wipro.ats.bdre.md.api.oozie.OozieHaltJob --process-id "+ getId().toString()+" -batchmarking \'+dict[\"initJobInfo.getTargetBatchMarkingSet()\"] \n"+
                "\tbash_output = subprocess.Popen(command,shell=True, stdin=subprocess.PIPE, stdout=subprocess.PIPE )\n" +
                "\tout,err = bash_output.communicate()\n"+
                "\tprint(\"out is \",out)\n"+
                "\tprint(\"err is \",err)\n"+
                "\tif(bash_output.returncode > 0):\n" +
                "\t\treturn '"+getTermNode().getName().replace('-', '_') +"'\n" +
                "\telse:\n" +
                "\t\treturn 'success'\n" +
                "\ndef f_"+ getName().replace('-','_')+"():\n" +
                "\t"+ getName().replace('-', '_')+".set_downstream("+ getTermNode().getName().replace('-', '_')+")\n" +
                getName().replace('-', '_')+" = BranchPythonOperator(task_id='"+getName().replace('-', '_')+"', python_callable="+getName().replace('-','_')+"_pc, dag=dag)\n";


}




}