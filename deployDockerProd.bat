@set targetIp=47.97.197.147
@set privateKey="D:\work file\Project\vpac.ppk"
@set module=wk-vpac
@set stackPath=/data/docker-compose/vpac/
@set image=cqwk-docker.pkg.coding.net/devops/private/%module%

@echo ----------------- docker build -----------------
@call docker build -t %image% .

@echo ----------------- docker push -----------------
@call docker push %image%

@echo ----------------- copy stack file -----------------
plink -P 22 -no-antispoof -l root -i %privateKey% %targetIp%  "mkdir -p %stackPath%"
pscp -P 22 -l root -i %privateKey% docker-stack.yml %targetIp%:%stackPath%

@echo ----------------- deploy stack -----------------
plink -P 22 -no-antispoof -l root -i %privateKey% %targetIp%  ^
 "sleep 20 && docker stack deploy --with-registry-auth -c %stackPath%docker-stack.yml %module%"